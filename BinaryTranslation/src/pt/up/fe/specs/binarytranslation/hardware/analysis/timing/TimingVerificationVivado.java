package pt.up.fe.specs.binarytranslation.hardware.analysis.timing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import pt.up.fe.specs.binarytranslation.BinaryTranslationResource;
import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareFolderGenerator;
import pt.up.fe.specs.binarytranslation.processes.StringProcessRun;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.utilities.Replacer;

/** Generates a Vivado TCL script and runs a simulations on two devices (spartan 7 and veritex 7 ultrascale) and perform a timing analysis and power analysis
 * 
 *  <br>Note: due to the fact that currently all generated modules are combinational, vivado gives warnings about the power estimations being wrong,
 *   and in fact the power estimations the simulator gives are way to high (over 1W for an adder on a 16nm device, which is ridiculous)
 * 
 * @author Joao Conceicao
 *
 */
public class TimingVerificationVivado extends StringProcessRun{

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
    private String directory;
    private String moduleName;
    
    
    private static final String verificationResultsRegex = "Data Path Delay:\\s*([0-9]*.[0-9]*)ns\\s*\\S*\\s*([0-9]*.[0-9]*)ns\\s*\\(([0-9]*.[0-9]*)%\\)\\s*\\S*\\s*([0-9]*.[0-9]*)ns\\s*\\(([0-9]*.[0-9]*)%\\)";
    
    
    private static List<String> supportedDevices = List.of( // All of the devices chosen where in the smallest configuration possible in order to minimize any routing or implementation delays introduced by using a large device
            "xcau25p-ffvb676-1-e",  // This is a brand new Artix 7 Ultrascale+ 16nm device
            "xc7s6cpga196-1"    // This is an old Spartan 7 28nm device
            ); // Sadly the free version of Vivado does not include a license for any of the new Virtex Ultrascale+ devices. This would be usefull since they can operate at 600Mhz and have all the new tech that xilinx had
                // because of this the results will be worse than they could
    
    public TimingVerificationVivado(String directory, String moduleName) {
        super(TimingVerificationVivado.getArgs(directory, moduleName));
        
        this.directory = directory;
        this.moduleName = moduleName;
    }
    
    /** Reads the timing report files generated by Vivado, and applies a regex to extract the relevant information
     * 
     * 
     * 
     * @param directory Root of directory created by the tool chain for the segment
     * @param moduleName Name of the module
     * @return Map of the extracted values per supported device [String nameOfDevice, List(Float)[Total delay, Logic delay, Logical delay %, Route delay, Route delay %]]
     * @throws FileNotFoundException If one of the supported devices timing report file does not exist, this exception will be thrown
     */
    public static Map<String, List<Float>> getVerificationResultsParsed(String directory, String moduleName) throws FileNotFoundException{
        
        Map<String, List<Float>> verificationResults = new HashMap<>();
        
        for(String deviceName : TimingVerificationVivado.supportedDevices) {
        
            File verificationResultsFile = new File(directory + "/hw/reports/timing_" + deviceName + ".rpt");
            
            if(!verificationResultsFile.exists()) {
                throw new FileNotFoundException();
            }
            
            Scanner verificationScanner = new Scanner(verificationResultsFile);
    
            var results = verificationScanner.findAll(TimingVerificationVivado.verificationResultsRegex);
            
            results.forEach(result -> {
               
                List<Float> verificationNumerical = new ArrayList<>();
                
                for(int i = 0; i < 5; i++) {
                    verificationNumerical.add(i, Float.valueOf(result.group(i + 1)));
                }

                verificationResults.put(deviceName, verificationNumerical);
            });
        }
        
        return verificationResults;
    }
    
    private static List<String> getArgs(String directory, String moduleName) {

        var args = new ArrayList<String>();
        String vivadoTclScriptName = moduleName + ".tcl";
        
        args.add("cd " + HardwareFolderGenerator.getHardwareSynthesisFolder(directory));
        args.add("&&");
        args.add("vivado -mode tcl -source " + vivadoTclScriptName);
        return args;
    }

    @Override
    public Process start()  {
        
        try {
            TimingVerificationVivado.generateTCLScriptFile(this.directory, this.moduleName);
        } catch (IOException e) {
            return null;
        }
        
        return super.start();
    }
    
    /** Generates the TCL script used to run Vivado in TCL non project mode
     * 
     * @param moduleName Name of the module
     * @return The contents of the generated TCL script
     */
    public static String generateTCLScript(String moduleName) {
        
        var template = SpecsIo.getResource(BinaryTranslationResource.VIVADO_TCL_TIMING_VERIFICATION_TEMPLATE);
        var templatecontent = new Replacer(template);
        
        templatecontent.replace("<MODULE_NAME>", moduleName);
        
        return templatecontent.toString();
    }
    
    /** Generates and writes the TCL script used to run Vivado in TCL non project mode
     * 
     * @param directory Root of directory created by the tool chain for the segment 
     * @param moduleName Name of the generated module
     * @throws IOException If Java can't create a new file this exception will be thrown
     */
    public static void generateTCLScriptFile(String directory, String moduleName) throws IOException {
        
        String vivadoTclScriptName = moduleName + ".tcl";
        String vivadoTclScriptFullPath = HardwareFolderGenerator.getHardwareSynthesisFolder(directory)+"/"+vivadoTclScriptName;

        File vivadoTclScript = new File(vivadoTclScriptFullPath);
        
        if(!vivadoTclScript.createNewFile()) {
            System.out.println("Vivado TCL script already exists, it will be overwritten");
        }
        
        FileWriter vivadoTCLScriptWriter = new FileWriter(vivadoTclScriptFullPath);
        vivadoTCLScriptWriter.write(TimingVerificationVivado.generateTCLScript(moduleName));
        vivadoTCLScriptWriter.close();
    }

    
}

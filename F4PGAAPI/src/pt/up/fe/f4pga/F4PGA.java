package pt.up.fe.f4pga;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.utilities.Replacer;

public class F4PGA {

    String DEFAULT_F4PGA_INSTALL_DIR = "~/opt/f4pga";
    static FPGA validFPGA;
    F4PGAFlow flow;

    private ArrayList<FPGA> getFpgaList() {
        var basis3 = new FPGA("basis3", "xc7");
        var arty_35t = new FPGA("arty 35t", "xc7");
        var arty_100t = new FPGA("arty 100t", "xc7");
        var nexys4_ddr = new FPGA("nexys4 ddr", "xc7");
        var nexys_video = new FPGA("nexys video", "xc7");
        var zylo_z7 = new FPGA("zylo-z7", "xc7");

        ArrayList<FPGA> listFPGA = new ArrayList<>();
        listFPGA.add(basis3);
        listFPGA.add(arty_35t);
        listFPGA.add(arty_100t);
        listFPGA.add(nexys4_ddr);
        listFPGA.add(nexys_video);
        listFPGA.add(zylo_z7);

        return listFPGA;
    }

    public static List<String> getBuildCommands(String installDir) {

        var famDir = installDir + '/' + validFPGA.getFpgaFamily();
        var exportF4pgaCommand = "export PATH=" + installDir + "/install/bin:$PATH";
        var sourceCondaCommand = "source " + famDir + "/conda/etc/profile.d/conda.sh";
        var activateCondaCommand = "conda activate " + validFPGA.getFpgaFamily();
        var commandList = new ArrayList<String>();
        var buildCmd = "// f4pga build --flow ./flow.json";

        commandList.add(exportF4pgaCommand);
        commandList.add(sourceCondaCommand);
        commandList.add((String) activateCondaCommand);
        commandList.add(buildCmd);
        return commandList;
    }

    private List<String> getEnvCommmands(String installDir) {

        String famDir = installDir + '/' + validFPGA.getFpgaFamily();
        String exportF4pgaCommand = "export PATH=" + installDir + "/install/bin:$PATH";
        String sourceCondaCommand = "source " + famDir + "/conda/etc/profile.d/conda.sh";
        String activateCondaCommand = "conda activate " + validFPGA.getFpgaFamily();
        List<String> commandList = new ArrayList<>();
        commandList.add(exportF4pgaCommand);
        commandList.add(sourceCondaCommand);
        commandList.add(activateCondaCommand);
        return commandList;
    }

    private void getFPGA(String fpgaHwType) throws Exception {

        ArrayList<FPGA> list = getFpgaList();
        for (int index = 0; index < list.size(); index++) {
            FPGA currentFPGA = list.get(index);
            if (currentFPGA.getHwType().equalsIgnoreCase(fpgaHwType)) {
                validFPGA = currentFPGA;
                return;
            }
        }
        throw new Exception("Invalid FPGA");
    }

    /*public void init(String fpgaHwType) throws Exception {
    
        String F4pgaInstallDir = DEFAULT_F4PGA_INSTALL_DIR;
        // Checking if FPGA is supported by F4PGA
        getFPGA(fpgaHwType);
        List<String> commandList = getInitCommmands(F4pgaInstallDir);
        getResources();
    
        SpecsSystem.runProcess(commandList, false, true);
    }*/

    /*public void config(String hdlSourceDir, String topName) {
        flow.newFlow(hdlSourceDir, topName);
    }*/

    // private String doubleQouString;
    private List<String> hdlFilesListFromDir() {
        // list of HDL files
        var hdls = new ArrayList<String>();
        hdls.add("\"counter1.v\"");
        hdls.add("\"counter2.v\"");
        return hdls;
        // doubleQouString;
    }

    public void build(String buildDir) {

        var tmpl = F4PGAResource.F4PGABASH_SCRIPT_TEMPLATE;
        var tmplAsString = tmpl.read();

        var repl = new Replacer(tmplAsString);
        repl.replace("<SOURCE_LIST>", hdlFilesListFromDir());
        repl.replace("<PART_NUMBER>", F4PGATarget.Arty100T.getPartName());
        // repl.replace("<PART_NUMBER>", F4PGATarget.Basys3.getPartName());

        var tmplChanged = new File("./jsonChanged.json");
        SpecsIo.write(tmplChanged, repl.toString());

        /*=======
        
            public void build(String buildDir) {
        // fpgaFlowGenerate(String sourcePath, String topName)
        String target = validFPGA.getHwType();
        
        String buildCmd = "TARGET=/" + target + " make -C " + buildDir;
        var processOutput = SpecsSystem.runProcess(Arrays.asList(buildCmd), true, false);
        >>>>>>> summer_internship-man-fran*/

    }

    public void upload() {
    }
}

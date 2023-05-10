package pt.up.fe.specs.binarytranslation.processes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareFolderGenerator;
import pt.up.fe.specs.specshw.processes.StringProcessRun;

public class VerilatorMakeTestbench extends StringProcessRun {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    // TODO: this class should return an object of type VerilatorCompiledDesign, or something
    // and then we pass that class to VerilatorRun

    /*
     * Arguments to call Verilator on a particular
     * testbench and then call "make" on the resulting cpp code
     */
    
    private String directory;
    private String testbenchName;
    
    private static List<String> getArgs(String directory, String testbenchName) {
        
        var args = new ArrayList<String>();
        var verilatorexe = IS_WINDOWS ? "verilator.exe" : "verilator";

        args.add("make");
        args.add("--directory="+ HardwareFolderGenerator.getHardwareTestbenchesFolder(directory)+"/obj_dir");
        //args.add("./obj_dir");
        args.add("-f");
        args.add("V" + testbenchName + "_tb.mk");
        args.add("V" + testbenchName + "_tb");
   
        return args;
    }

    // TODO: remove the need for directory specification
    public VerilatorMakeTestbench(String directory, String testbenchName) {
        super(VerilatorMakeTestbench.getArgs(directory, testbenchName));
        
        this.directory = directory;
        this.testbenchName = testbenchName;
        
    }
    
    @Override
    public Process start() {
        
        ProcessBuilder builder = new ProcessBuilder(VerilatorMakeTestbench.getArgs(this.directory, this.testbenchName));
        builder.directory(new File(HardwareFolderGenerator.getHardwareTestbenchesFolder(directory)));
        
        try {
            this.proc = builder.start();
        } catch (IOException e) {
            return null;
        }
        
        //this.proc = SpecsHwUtils.newProcess(builder);
 
        this.attachThreads();
        
        return this.proc;
    }
    
}
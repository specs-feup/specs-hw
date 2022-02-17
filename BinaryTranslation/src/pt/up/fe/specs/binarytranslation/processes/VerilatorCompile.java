/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.binarytranslation.processes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareFolderGenerator;
import pt.up.fe.specs.specshw.SpecsHwUtils;

public class VerilatorCompile extends StringProcessRun {

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

        args.add(verilatorexe);
        args.add("-cc");
        args.add(HardwareFolderGenerator.getHardwareTestbenchesFolder(directory) + "/" + testbenchName + "_tb.sv");
        args.add(HardwareFolderGenerator.getHardwareHDLFolder(directory) + "/" + testbenchName + ".sv"); // TODO: whats this argument for?
        args.add("-exe");
        args.add(HardwareFolderGenerator.getHardwareTestbenchesFolder(directory) + "/" + testbenchName + "_tb.cpp");
   
        return args;
    }

    // TODO: remove the need for directory specification
    public VerilatorCompile(String directory, String testbenchName) {
        super(VerilatorCompile.getArgs(directory, testbenchName));
        
        this.directory = directory;
        this.testbenchName = testbenchName;
        
    }
    
    @Override
    public Process start() {
        
        ProcessBuilder builder = new ProcessBuilder(VerilatorCompile.getArgs(this.directory, this.testbenchName));
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

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

package pt.up.fe.specs.binarytranslation.hardware.testbench;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.up.fe.specs.util.SpecsLogs;

public class VerilatorRun{

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
    
    
    public static boolean start(String directory, String testbenchName) throws IOException {
        VerilatorRun.verilate(directory, testbenchName);
        
        return VerilatorRun.simulate(directory, testbenchName);
    }
    
    public static void verilate(String directory, String testbenchName) throws IOException {

        StringBuilder processCommands = new StringBuilder();
        
        if(VerilatorRun.IS_WINDOWS)
            processCommands.append("bash -c \"");
        
        processCommands.append("cd " + directory + ";");
        processCommands.append("verilator -cc ./" + testbenchName + "_tb.sv ../hdl/" + testbenchName + ".sv -exe ./" + testbenchName + "_tb.cpp;");
        processCommands.append("make -C obj_dir -f V" + testbenchName + "_tb.mk V" + testbenchName + "_tb;");
        
        if(VerilatorRun.IS_WINDOWS)
            processCommands.append("\"");
        
        ProcessBuilder processBuilder = new ProcessBuilder();
        
        Process process = processBuilder.command("cmd.exe", "/c", processCommands.toString()).start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        }
            
        
    }
    
    public static boolean simulate(String directory, String testbenchName) throws IOException {
        
        StringBuilder processCommands = new StringBuilder();
        
        if(VerilatorRun.IS_WINDOWS)
            processCommands.append("bash -c \"");
        
        processCommands.append("cd " + directory + ";");
        processCommands.append("./obj_dir/V" + testbenchName + "_tb;");
        
        if(VerilatorRun.IS_WINDOWS)
            processCommands.append("\"");

     
        
        ProcessBuilder processBuilder = new ProcessBuilder();
        
        Process process = processBuilder.command("cmd.exe", "/c", processCommands.toString()).start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        String line;
        String returnLine = "";
        
        
        while((line = reader.readLine()) != null){
            returnLine = line;
        }
        
        
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        }
        
        System.out.println("\t" + returnLine);
        
        return returnLine.equals("PASSED") ? true : false;
      
    }
    
}

/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.hardware.validation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GDBRunner {

    
    
    public static void write(Process process, String processInput) throws IOException {
        process.getOutputStream().write(processInput.getBytes());
        process.getOutputStream().flush();
    }
    
    public static String read(BufferedReader processOutput) throws IOException {
        
        String line;
        StringBuilder readOutput = new StringBuilder();
        
        while ((line = processOutput.readLine()) != null) {
            readOutput.append(line + "\n");
        }
        
        return readOutput.toString();
    }
    
    
    
   public static void run(String elfFile) throws IOException {
       
       ProcessBuilder processBuilder = new ProcessBuilder();

       Pattern regexPattern = Pattern.compile("(([a-zA-Z0-9]+)\\s+(0x[0-9a-f]+)\\s+([0-9]+))");
       
       processBuilder.command("gdb -q " + elfFile);
       
       Process process = processBuilder.start();
       
       BufferedReader gdbOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
       
       String initialBreakpoint = new String();
       String finalBreakpoint = new String();
       
       List<String> result = new ArrayList<>();
       
       GDBRunner.write(process, initialBreakpoint);
       GDBRunner.read(gdbOutput);
       
       GDBRunner.write(process, finalBreakpoint);
       GDBRunner.read(gdbOutput);
       
       GDBRunner.write(process, "run");
       GDBRunner.read(gdbOutput);
       
       
       // Set relevant registers
       GDBRunner.write(process, "");
       GDBRunner.read(gdbOutput);
       
       GDBRunner.write(process, "continue");
       GDBRunner.read(gdbOutput);
       
       GDBRunner.write(process, "stepi");
       GDBRunner.read(gdbOutput);
       
       GDBRunner.write(process, "info registers");
       result.add(GDBRunner.read(gdbOutput));
       
    
       
   }
    
}

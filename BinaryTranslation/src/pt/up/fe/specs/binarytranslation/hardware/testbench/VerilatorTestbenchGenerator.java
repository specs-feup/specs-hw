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

package pt.up.fe.specs.binarytranslation.hardware.testbench;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.Scanner;

import pt.up.fe.specs.util.SpecsLogs;

public class VerilatorTestbenchGenerator {

    public static void emit(OutputStream output, String moduleName, int samples) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
            bw.write(VerilatorTestbenchGenerator.emit(moduleName, samples));
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String emit(String moduleName, int samples) throws IOException {
        
        StringBuilder fileBuilder = new StringBuilder();
        File templateFile;
        
        try {
            templateFile = new File(VerilatorTestbenchGenerator.class.getClassLoader().getResource("pt/up/fe/specs/binarytranslation/hardware/verilator/verilator_testbench_template.cpp").toURI());
        } catch (URISyntaxException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
            return null;
        }
        
        Scanner templateScanner = new Scanner(templateFile);
        
        while(templateScanner.hasNextLine()) {
            fileBuilder.append(templateScanner.nextLine().replace("<TESTBENCHNAME>", moduleName + "_tb").replace("<NUMBEROFSAMPLES>", String.valueOf(samples)) + "\n");
        }
        
        templateScanner.close();
        
        return fileBuilder.toString();  
    }

    
}

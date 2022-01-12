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

package pt.up.fe.specs.binarytranslation.instruction.calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestbenchValidationFilesGenerator {

    private static Pattern infoRegisterPattern = Pattern.compile("(?:Breakpoint [0-9]+,.+\n((?:[a-zA-Z0-9]+\s+[a-zA-Z0-9]+\s+.+\n)+))"); // Gets all of the info registers in group1
    private static Pattern registerPattern = Pattern.compile("(?:([a-zA-Z0-9]+)\s+(0x[0-9a-f]+)\s+(?:.+))"); // Gets all registers in instruction g1 register name g2 value
    
    public static String generateOutputsValidationFile(String gdbfile) {
        
        StringBuilder validationBuilder = new StringBuilder();
        Matcher infoRegisterMatcher = TestbenchValidationFilesGenerator.infoRegisterPattern.matcher(gdbfile);
        
        infoRegisterMatcher.results().forEach(match -> {
            Matcher registerMatcher = TestbenchValidationFilesGenerator.registerPattern.matcher(match.toString());
            
            registerMatcher.results().forEach(register -> {
                validationBuilder.append(register.group(2).replace("0x", ""));
                validationBuilder.append(" ");
            });
            
            validationBuilder.append("\n");
        });
        
        return validationBuilder.toString();
    }
    
    
}

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

public class GenerateMakefile {

    public static String emit(String moduleName) {
        
        StringBuilder fileBuilder = new StringBuilder();
        
        fileBuilder.append("help:\n\t@echo \"verilate, run, clean\"\n\nverilate: clean");
        
        fileBuilder.append("\n\t@verilator --assert -cc " + moduleName + ".sv custom_instruction_unit.sv -exe " + moduleName + ".cpp");
        fileBuilder.append("\n\t@make -C obj_dir -f V" + moduleName + ".mk V" + moduleName + "\n");
        fileBuilder.append("\nrun:\n\t./obj_dir/V" + moduleName + "\n");
        
        fileBuilder.append("\nclean:\n\t@rm -f -r ./obj_dir/\n");
        
        return fileBuilder.toString();
    }
    
}

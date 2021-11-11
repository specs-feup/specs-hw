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

public class VerilatorTestbenchGenerator {

    public static String emit(String testbench_name, int samples) {
        
        StringBuilder fileBuilder = new StringBuilder();
        
        fileBuilder.append("#include <stdlib.h>\n#include <iostream>\n#include <verilated.h>\n#include <verilated_vcd_c.h>\n");
        
        fileBuilder.append("\n#include \"V" + testbench_name + ".h\"\n");
        fileBuilder.append("\n#define VALIDATION_SAMPLES " + String.valueOf(samples) + "\n");
        
        fileBuilder.append("#define VERIFICATION_FAIL 0\n"
                + "#define VERIFICATION_OK 1\n\n"
                + "int main(int argc, char **argv)\n{\n\n"
                + "  V");
        fileBuilder.append(testbench_name);
        
        fileBuilder.append(" *tb = new V");
        
        fileBuilder.append(testbench_name + ";\n\n");
        
        fileBuilder.append("  for(int i = VALIDATION_SAMPLES; i > 0; i--){\n\n"
                + "    tb->verify = 1;\n\n"
                + "    for(int w = 0; w < 100; w++)\r\n"
                + "      tb->eval();\r\n"
                + "\r\n"
                + "    tb->verify = 0;\r\n"
                + "    tb->eval();\r\n"
                + "\r\n"
                + "    if(tb->verifyResults == VERIFICATION_FAIL) {\r\n"
                + "      delete tb;\r\n"
                + "      exit(-1);\r\n"
                + "    }\n\n"
                + "  }\n\n"
                + "  delete tb;\r\n"
                + "  exit(EXIT_SUCCESS);\n\n"
                + "}");
        
        return fileBuilder.toString();
    }
    
}

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

import pt.up.fe.specs.binarytranslation.detection.segments.ABinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class GDBTesterFileGenerator {

    public static String exportInital() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("set confirm off\n");
        
        return builder.toString();
    }
    
    public static String export(Instruction instruction) {
        
        StringBuilder builder = new StringBuilder();
        
        
        
        builder.append(GDBTesterFileGenerator.addBreakpoint(instruction));
        
        builder.append("run\n");
        
        builder.append("info registers\n");
        
        builder.append("quit\n");
        
        return builder.toString();
    }
    
    public static String export(ABinarySegment segment) {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("set confirm off\n");
        
        builder.append(GDBTesterFileGenerator.addBreakpoint(segment.getInstructions().get(0)));
        builder.append(GDBTesterFileGenerator.addBreakpoint(segment.getInstructions().get(segment.getInstructions().size() - 1)));
        
      
        
        builder.append("run\n");
        
        builder.append("continue\n;");
        builder.append("info registers\n");
        builder.append("quit\n");
        
        return builder.toString();
    }
    
    public static String addBreakpoint(Instruction instruction) {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("break *0x");
        builder.append(Long.toHexString(instruction.getAddress()));
        builder.append("\n");
        return builder.toString();
    }
}

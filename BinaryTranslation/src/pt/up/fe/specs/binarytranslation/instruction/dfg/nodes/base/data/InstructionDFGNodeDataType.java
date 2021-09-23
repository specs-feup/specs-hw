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

package pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.data;


public enum InstructionDFGNodeDataType {
    
    Register,
    Immediate,
    Flag;
    
    static public InstructionDFGNodeDataType getType(String data) {
       
        if(data.contains("r")) {
            return InstructionDFGNodeDataType.Register;
        }
        
        try {
            Integer.parseInt(data);
            
            return InstructionDFGNodeDataType.Immediate;
        }catch(NumberFormatException e) {
            return InstructionDFGNodeDataType.Flag;
        }
        
    }
    
    public boolean sameTypeAs(InstructionDFGNodeDataType node_type) {
        if(this.equals(node_type)) {
            return true;
        }
         return false;   
    }
    
}

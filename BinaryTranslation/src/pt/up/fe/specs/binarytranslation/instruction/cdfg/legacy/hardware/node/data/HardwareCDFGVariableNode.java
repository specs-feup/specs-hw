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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.hardware.node.data;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.data.GenericCDFGVariableNode;

public class HardwareCDFGVariableNode extends GenericCDFGVariableNode{

    private boolean input;
    private boolean combinational;
    private int upper;
    private int lower;
    
    public HardwareCDFGVariableNode(String reference, boolean input, int bw) {
        super(reference);
        this.combinational = true;
        this.upper = bw;
        this.lower = 0;
    }

    public String IODeclaration() {
        
        String declaration = new String();
        
        declaration += (this.input) ? "input " : "output ";
        declaration += (this.combinational) ? "wire " : "reg ";
        declaration += (this.upper != 0) ? ("[" + String.valueOf(this.upper) + ":" + String.valueOf(this.lower) +"]") : "";
        declaration += this.getName() + ";\n";
        
        return declaration;
    }
    
}

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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;

public abstract class AInstructionCDFGControlNode extends AInstructionCDFGNode{

    protected AInstructionCDFGControlNode(String reference) {
        super(reference);
    }

    @Override
    public Attribute getDOTLabel() {
        return DefaultAttribute.createAttribute("");
    }
    
    @Override
    public Attribute getDOTShape() {
        return DefaultAttribute.createAttribute("diamond");
    }
    
}

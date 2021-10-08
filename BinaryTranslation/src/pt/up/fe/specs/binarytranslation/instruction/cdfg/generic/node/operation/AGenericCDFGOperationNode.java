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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.AGenericCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.GenericCDFGNodeGenerator;

public abstract class AGenericCDFGOperationNode extends AGenericCDFGNode implements GenericCDFGNodeGenerator{

    private String operation;
    private int precedence;
    
    protected AGenericCDFGOperationNode(IGenericCDFGOperationNodeType operation) {
        
        super(operation);
        
        this.precedence = operation.operatorPrecedence();
        this.operation = operation.operation();
 
    }
    
    public String getOperation() {
        return this.operation;
    }
    
    @Override
    public String toString() {
        return this.operation;
    }
    
    @Override
    public String getDotShape() {
        return "box";
    }
    
    public int operatorPrecedence() {
        return this.precedence;
    }
}

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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.AGenericCDFGOperationNode;

public abstract class AGenericCDFGExpressionNode extends AGenericCDFGOperationNode{

    private boolean unary;
    private boolean commutative;
    private boolean associative;
    
    protected AGenericCDFGExpressionNode(IGenericCDFGExpressionNodeType expression) {
        
       super(expression);
       
       this.unary = expression.isUnary();
       this.commutative = expression.isCommutative();
       this.associative = expression.isAssociative();
    }

    public boolean isUnary() {
        return this.unary;
    }
    
    public boolean isCommutative() {
        return this.commutative;
    }
    
    public boolean isAssociative() {
        return this.associative;
    }
    
    
}

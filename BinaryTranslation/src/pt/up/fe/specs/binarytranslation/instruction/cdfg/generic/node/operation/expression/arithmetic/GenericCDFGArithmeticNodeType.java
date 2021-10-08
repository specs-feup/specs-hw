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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.IGenericCDFGOperationNodeType;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.IGenericCDFGExpressionNodeType;

public enum GenericCDFGArithmeticNodeType implements IGenericCDFGExpressionNodeType{
    
    Addition("+","add",6,true,true),
    Division("/","div",5,false,false),
    Multiplication("*","mul",5,true,true),
    ShiftLeftLogical("<<","sll",7,false,false),
    ShiftRightArithmetic(">>>","sra",7,false,false),
    ShiftRightLogical(">>","srl",7,false,false),
    SquareRoot("sqrt","sqrt",2,false,false),
    Subtraction("-","sub",6,false,false);
    
    private final String operation;
    private final String name;
    private final int precedence;
    private final boolean commutative;
    private final boolean associative;
    
    private GenericCDFGArithmeticNodeType(String operation, String name, int precedence, boolean associative, boolean commutative) {
        this.operation = operation;
        this.name = name;
        this.precedence = precedence;
        this.commutative = commutative;
        this.associative = associative;
    }
    
    public String operation() {
        return this.operation;
    }
    
    @Override
    public String toString() {
        return this.operation;
    }

    public String getName() {
        return this.name;
    }
    
    public int operatorPrecedence() {
        return this.precedence;
    }
    
    public boolean isCommutative() {
        return this.commutative;
    }
    
    public boolean isAssociative() {
        return this.associative;
    }
    
    public boolean isUnary() {
        return false;
    }
}

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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.bitwise;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.IGenericCDFGExpressionNodeType;

public enum GenericCDFGBitwiseNodeType implements IGenericCDFGExpressionNodeType{

        BitwiseOr("|","bor",13),
        BitwiseXor("^","bxor",12),
        BitwiseAnd("&","band",11),
        BitwiseNot("~","bnot",3, true),
        Assign("=","ass",16, true);
    
    private final String operation;
    private final String name;
    private final int predecedence;
    private final boolean unary;
    
    private GenericCDFGBitwiseNodeType(String operation, String name, int precedence) {
        this.operation = operation;
        this.name = name;
        this.predecedence = precedence;
        this.unary = false;
    }
    
    private GenericCDFGBitwiseNodeType(String operation, String name, int precedence, boolean unary) {
        this.operation = operation;
        this.name = name;
        this.predecedence = precedence;
        this.unary = unary;
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
        return this.predecedence;
    }
    
    public boolean isCommutative() {
        return false;
    }
    
    public boolean isAssociative() {
        return false;
    }
    
    public boolean isUnary() {
        return this.unary;
    }
}

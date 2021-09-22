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

package pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.operation.math;

public enum InstructionDFGMathOperationNodeType {

    Addition,
    Subtraction,
    Multiplication,
    Division,
    OR,
    XOR,
    AND,
    ShiftLeftLogical,
    ShiftRightLogical,
    ShiftRightArithmetic;
    
    @Override
    public String toString() {
        switch(this) {
            case Addition:
                return "+";
            case Subtraction:
                return "-";
            case Multiplication:
                return "*";
            case Division:
                return "/";
            case OR:
                return "|";
            case XOR:
                return "^";
            case AND:
                return "&";
            default:
                return "";
        }
    }
    
    /** Determines which element of InstructionDFGMathOperationNodeType String operator refers to
     * 
     * @return The corresponding element of InstructionDFGMathOperationNodeType that corresponds to the operator passed
     */
    static public InstructionDFGMathOperationNodeType getType(String operator) {
        switch(operator) {
            case "+":
                return InstructionDFGMathOperationNodeType.Addition;
            case "-":
                return InstructionDFGMathOperationNodeType.Subtraction;
            case "*":
                return InstructionDFGMathOperationNodeType.Multiplication;
            case "/":
                return InstructionDFGMathOperationNodeType.Division;
            case "&":
                return InstructionDFGMathOperationNodeType.AND;
            case "^":
                return InstructionDFGMathOperationNodeType.XOR;
            case "|":
                return InstructionDFGMathOperationNodeType.OR;
            case "<<":
                return InstructionDFGMathOperationNodeType.ShiftLeftLogical;
            case ">>":
                return InstructionDFGMathOperationNodeType.ShiftRightLogical;
            case ">>>":
                return InstructionDFGMathOperationNodeType.ShiftRightArithmetic;
            default:
                return null;
        }
    }
    
    /** Meant for allowing the implementation of optimizations
     * 
     * @return True if the math operation node type is associative
     */
    public boolean isAssociative() {
        switch(this) {
            case Addition:
                return true;
            case Multiplication:
                return true;
            default:
                return false;
        }
    }
    
    
    /** Meant for allowing the implementation of optimizations
     * 
     * @return True if the math operation node type is commutative
     */
    public boolean isCommutative() {
        switch(this) {
            case Addition:
                return true;
            case Multiplication:
                return true;
            case OR:
                return true;
            case XOR:
                return true;
            case AND:
                return true;
            default:
                return false;
        }
    }
    
    public int getOperationPrecedence() {
        switch(this) {
            case Addition:
                return 4;
            case Subtraction:
                return 4;
            case Multiplication:
                return 3;
            case Division:
                return 3;
            default:
                return 5;
        }
    }
    
    public boolean hasPrecedence(InstructionDFGMathOperationNodeType math_operation) {
        return (this.getOperationPrecedence() < math_operation.getOperationPrecedence());
    }
}

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

package pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.operation.comparison;

public enum InstructionDFGComparisonOperationNodeType {

    EqualsTo,
    NotEqualsTo,
    LessThan,
    GreaterThan,
    GreaterThanOrEqualsTo,
    LessThanOrEqualsTo;
    
    @Override
    public String toString() {
        switch(this) {
            case EqualsTo:
                return "==";
            case NotEqualsTo:
                return "!=";
            case LessThan:
                return "<";
            case GreaterThan:
                return ">";
            case GreaterThanOrEqualsTo:
                return ">=";
            case LessThanOrEqualsTo:
                return "<=";
            default:
                return null;
        }
    }
    
    public static boolean isComparisonOperator(String operator) {
        return !(InstructionDFGComparisonOperationNodeType.getType(operator).equals(null));
    }
    
    public static InstructionDFGComparisonOperationNodeType getType(String operator) {
        switch(operator) {
            case "<":
                return InstructionDFGComparisonOperationNodeType.LessThan;
            case ">":
                return InstructionDFGComparisonOperationNodeType.GreaterThan;
            case "==":
                return InstructionDFGComparisonOperationNodeType.EqualsTo;
            case "!=":
                return InstructionDFGComparisonOperationNodeType.NotEqualsTo;
            case "<=":
                return InstructionDFGComparisonOperationNodeType.LessThanOrEqualsTo;
            case ">=":
                return InstructionDFGComparisonOperationNodeType.GreaterThanOrEqualsTo;
            default:
                return null;
        }
    }
    
    
}

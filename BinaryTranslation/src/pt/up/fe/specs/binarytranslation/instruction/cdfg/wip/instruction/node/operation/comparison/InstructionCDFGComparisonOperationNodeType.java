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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation.comparison;

import java.lang.reflect.Constructor;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation.AInstructionCDFGOperationNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation.InstructionCDFGOperationNode;
import pt.up.fe.specs.util.SpecsLogs;

public enum InstructionCDFGComparisonOperationNodeType implements InstructionCDFGOperationNode{
    
    EqualsTo("==", "eq", InstructionCDFGEqualsToNode.class),
    NotEqualsTo("!=", "neq", InstructionCDFGNotEqualsToNode.class),
    GreaterThan(">", "gt", InstructionCDFGGreaterThanNode.class),
    LessThan("<", "lt", InstructionCDFGLessThanNode.class),
    GreaterThanOrEqualsTo(">=", "gtet", InstructionCDFGGreaterThanOrEqualsToNode.class),
    LessThanOrEqualsTo("<=", "ltet", InstructionCDFGLessThanOrEqualsToNode.class)
    
    ;
    
    private final String operator;
    private final String operator_name;
    private final Constructor<? extends AInstructionCDFGOperationNode> constructor;
    
    private InstructionCDFGComparisonOperationNodeType(String operator, String operator_name, Class<? extends AInstructionCDFGOperationNode> operation_class) {
        
        this.operator = operator;
        this.operator_name = operator_name;
        
        Constructor<? extends AInstructionCDFGOperationNode> constructor_tmp;
        
        try {
            constructor_tmp = operation_class.getConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
            constructor_tmp = null;
        }
        
        this.constructor = constructor_tmp;
        
    }
    
    public String getOperator() {
        return this.operator;
    }
    
    public String getOperatorName() {
        return this.operator_name;
    }
    
    public Constructor<? extends AInstructionCDFGOperationNode> getConstructor(){
        return this.constructor;
    }
    
}

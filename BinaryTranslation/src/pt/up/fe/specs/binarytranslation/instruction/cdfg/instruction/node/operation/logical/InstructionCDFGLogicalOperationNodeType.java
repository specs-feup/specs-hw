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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.logical;

import java.lang.reflect.Constructor;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.AInstructionCDFGOperationNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.InstructionCDFGOperationNode;
import pt.up.fe.specs.util.SpecsLogs;

public enum InstructionCDFGLogicalOperationNodeType implements InstructionCDFGOperationNode{
    
    LogicalAnd("&&", "land", InstructionCDFGLogicalAndNode.class),
    LogicalOr("||", "lor", InstructionCDFGLogicalOrNode.class),
    LogicalNot("!", "lnot", InstructionCDFGLogicalNotNode.class)
    
    ;
    
    private final String operator;
    private final String operator_name;
    private final Constructor<? extends AInstructionCDFGOperationNode> constructor;
    
    private InstructionCDFGLogicalOperationNodeType(String operator, String operator_name, Class<? extends AInstructionCDFGOperationNode> operation_class) {
        
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

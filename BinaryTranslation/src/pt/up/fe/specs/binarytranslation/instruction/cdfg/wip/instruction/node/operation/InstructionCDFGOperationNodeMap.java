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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation.arithmetic.InstructionCDFGArithmeticOperationNodeType;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation.bitwise.InstructionCDFGBitwiseOperationNodeType;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation.comparison.InstructionCDFGComparisonOperationNodeType;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation.logical.InstructionCDFGLogicalOperationNodeType;
import pt.up.fe.specs.util.SpecsLogs;

public class InstructionCDFGOperationNodeMap {

    public static final Map<String, Constructor<? extends AInstructionCDFGOperationNode>> MAP;
    
    public static AInstructionCDFGOperationNode generate(String key, Object ... parameters) {
        try {

            Constructor<? extends AInstructionCDFGOperationNode> constructor = MAP.get(key);
            
            if(constructor == null) {
                throw new IllegalArgumentException();
            }
            
            return  constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
            return null;
        }
        
    }
    
    static {
        var map = new HashMap<String, Constructor<? extends AInstructionCDFGOperationNode>>();
        
        
        // Arithmetic Nodes
        map.put(InstructionCDFGArithmeticOperationNodeType.Addition.getOperator(), InstructionCDFGArithmeticOperationNodeType.Addition.getConstructor());
        map.put(InstructionCDFGArithmeticOperationNodeType.Subtraction.getOperator(), InstructionCDFGArithmeticOperationNodeType.Subtraction.getConstructor());
        map.put(InstructionCDFGArithmeticOperationNodeType.Multiplication.getOperator(), InstructionCDFGArithmeticOperationNodeType.Multiplication.getConstructor());
        map.put(InstructionCDFGArithmeticOperationNodeType.Division.getOperator(), InstructionCDFGArithmeticOperationNodeType.Division.getConstructor());
        map.put(InstructionCDFGArithmeticOperationNodeType.ShiftRightArithmetic.getOperator(), InstructionCDFGArithmeticOperationNodeType.ShiftRightArithmetic.getConstructor());
        map.put(InstructionCDFGArithmeticOperationNodeType.ShiftRightLogical.getOperator(), InstructionCDFGArithmeticOperationNodeType.ShiftRightLogical.getConstructor());
        map.put(InstructionCDFGArithmeticOperationNodeType.ShiftLeftLogical.getOperator(), InstructionCDFGArithmeticOperationNodeType.ShiftLeftLogical.getConstructor());
        
        
        // Bitwise Nodes
        map.put(InstructionCDFGBitwiseOperationNodeType.BitwiseAnd.getOperator(), InstructionCDFGBitwiseOperationNodeType.BitwiseAnd.getConstructor());
        map.put(InstructionCDFGBitwiseOperationNodeType.BitwiseNot.getOperator(), InstructionCDFGBitwiseOperationNodeType.BitwiseNot.getConstructor());
        map.put(InstructionCDFGBitwiseOperationNodeType.BitwiseXor.getOperator(), InstructionCDFGBitwiseOperationNodeType.BitwiseXor.getConstructor());
        map.put(InstructionCDFGBitwiseOperationNodeType.BitwiseOr.getOperator(), InstructionCDFGBitwiseOperationNodeType.BitwiseOr.getConstructor());
        
        // Comparison Nodes
        
        map.put(InstructionCDFGComparisonOperationNodeType.EqualsTo.getOperator(), InstructionCDFGComparisonOperationNodeType.EqualsTo.getConstructor());
        map.put(InstructionCDFGComparisonOperationNodeType.NotEqualsTo.getOperator(), InstructionCDFGComparisonOperationNodeType.NotEqualsTo.getConstructor());
        map.put(InstructionCDFGComparisonOperationNodeType.GreaterThan.getOperator(), InstructionCDFGComparisonOperationNodeType.GreaterThan.getConstructor());
        map.put(InstructionCDFGComparisonOperationNodeType.LessThan.getOperator(), InstructionCDFGComparisonOperationNodeType.LessThan.getConstructor());
        map.put(InstructionCDFGComparisonOperationNodeType.GreaterThanOrEqualsTo.getOperator(), InstructionCDFGComparisonOperationNodeType.GreaterThanOrEqualsTo.getConstructor());
        map.put(InstructionCDFGComparisonOperationNodeType.LessThanOrEqualsTo.getOperator(), InstructionCDFGComparisonOperationNodeType.LessThanOrEqualsTo.getConstructor());
        
        // Logical Nodes
        
        map.put(InstructionCDFGLogicalOperationNodeType.LogicalAnd.getOperator(), InstructionCDFGLogicalOperationNodeType.LogicalAnd.getConstructor());
        map.put(InstructionCDFGLogicalOperationNodeType.LogicalNot.getOperator(), InstructionCDFGLogicalOperationNodeType.LogicalNot.getConstructor());
        map.put(InstructionCDFGLogicalOperationNodeType.LogicalOr.getOperator(), InstructionCDFGLogicalOperationNodeType.LogicalOr.getConstructor());
        
        MAP = Collections.unmodifiableMap(map);
    }
    
}

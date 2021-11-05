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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.DivisionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.LeftShiftExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.MultiplicationExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.RightArithmeticShiftExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.RightLogicalShiftExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.SubtractionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.bitwise.BitWiseAndExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.bitwise.BitWiseNotExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.bitwise.BitWiseOrExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.bitwise.BitWiseXorExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.EqualsToExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.GreaterThanExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.GreaterThanOrEqualsToExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.LessThanExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.LessThanOrEqualsToExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.NotEqualsToExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.logical.LogicalAndExpression;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGAdditionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGAssignmentNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGDivisionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGMultiplicationNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGShiftLeftLogicalNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGShiftRightArithmeticNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGShiftRightLogicalNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGSubtractionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.bitwise.InstructionCDFGBitwiseAndNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.bitwise.InstructionCDFGBitwiseNotNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.bitwise.InstructionCDFGBitwiseOrNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.bitwise.InstructionCDFGBitwiseXorNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.comparison.InstructionCDFGEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.comparison.InstructionCDFGGreaterThanNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.comparison.InstructionCDFGGreaterThanOrEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.comparison.InstructionCDFGLessThanNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.comparison.InstructionCDFGLessThanOrEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.comparison.InstructionCDFGNotEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.logical.InstructionCDFGLogicalAndNode;
import pt.up.fe.specs.util.SpecsLogs;

public class HardwareNodeExpressionMap{

    public static final Map<Class<? extends AInstructionCDFGNode>, Class<? extends HardwareExpression>> MAP;

    public static HardwareExpression generate(Class<? extends AInstructionCDFGNode> key, List<HardwareExpression> operand ) throws IllegalArgumentException{
        
        if(operand.size() > 2) {
            throw new IllegalArgumentException();
        }
        
        try {
 
            Constructor<? extends HardwareExpression> constructor;
            
            if(operand.size() == 1) {
                if(key.equals(InstructionCDFGAssignmentNode.class)) {
                    constructor = MAP.get(key).getConstructor(String.class);
                }else {
                    constructor = MAP.get(key).getConstructor(HardwareExpression.class);
                }
            }else {
                constructor = MAP.get(key).getConstructor(HardwareExpression.class,HardwareExpression.class);
            }
            
            if(constructor == null) {
                throw new IllegalArgumentException();
            }
            
            if(operand.size() == 1) {
                
                if(key.equals(InstructionCDFGAssignmentNode.class)) {
                    return  constructor.newInstance(operand.get(0).getAsString());
                }
                
                return  constructor.newInstance(operand.get(0));
            }else {
                return  constructor.newInstance(operand.get(0), operand.get(1));
            }
            
            
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            //SpecsLogs.msgWarn("Error message:\n", e);
            return null;
        }
        
    }
    
    
    
    static {
        
        var map = new HashMap<Class<? extends AInstructionCDFGNode>, Class<? extends HardwareExpression>>();
        
        map.put(InstructionCDFGAssignmentNode.class, VariableReference.class);
        
        map.put(InstructionCDFGAdditionNode.class, AdditionExpression.class);
        map.put(InstructionCDFGSubtractionNode.class, SubtractionExpression.class);
        map.put(InstructionCDFGMultiplicationNode.class, MultiplicationExpression.class);
        map.put(InstructionCDFGShiftLeftLogicalNode.class, LeftShiftExpression.class);
        map.put(InstructionCDFGShiftRightArithmeticNode.class, RightArithmeticShiftExpression.class);
        map.put(InstructionCDFGShiftRightLogicalNode.class, RightLogicalShiftExpression.class);
        map.put(InstructionCDFGDivisionNode.class, DivisionExpression.class);
        
        map.put(InstructionCDFGBitwiseAndNode.class, BitWiseAndExpression.class);
        map.put(InstructionCDFGBitwiseOrNode.class, BitWiseOrExpression.class);
        map.put(InstructionCDFGBitwiseXorNode.class, BitWiseXorExpression.class);
        map.put(InstructionCDFGBitwiseNotNode.class, BitWiseNotExpression.class);
        
        map.put(InstructionCDFGLogicalAndNode.class, LogicalAndExpression.class);
        
        map.put(InstructionCDFGEqualsToNode.class, EqualsToExpression.class);
        map.put(InstructionCDFGNotEqualsToNode.class, NotEqualsToExpression.class);
        map.put(InstructionCDFGLessThanNode.class, LessThanExpression.class);
        map.put(InstructionCDFGGreaterThanNode.class, GreaterThanExpression.class);
        map.put(InstructionCDFGLessThanOrEqualsToNode.class, LessThanOrEqualsToExpression.class);
        map.put(InstructionCDFGGreaterThanOrEqualsToNode.class, GreaterThanOrEqualsToExpression.class);
        
        
        MAP = Collections.unmodifiableMap(map);
    }
}

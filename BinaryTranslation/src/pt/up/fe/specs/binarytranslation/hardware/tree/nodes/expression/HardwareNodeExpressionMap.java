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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGAdditionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGMultiplicationNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGShiftLeftLogicalNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGSubtractionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.bitwise.InstructionCDFGBitwiseAndNode;
import pt.up.fe.specs.util.SpecsLogs;

public class HardwareNodeExpressionMap{

    public static final Map<Class<? extends AInstructionCDFGNode>, Class<? extends HardwareExpression>> MAP;

    public static HardwareExpression generate(Class<? extends AInstructionCDFGNode> key) {
        try {

            Constructor<? extends HardwareExpression> constructor = MAP.get(key).getConstructor();
            
            if(constructor == null) {
                throw new IllegalArgumentException();
            }
            
            return  constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
            return null;
        }
        
    }
    
    
    static {
        
        var map = new HashMap<Class<? extends AInstructionCDFGNode>, Class<? extends HardwareExpression>>();
        
        map.put(InstructionCDFGAdditionNode.class, AdditionExpression.class);
        map.put(InstructionCDFGSubtractionNode.class, SubtractionExpression.class);
        map.put(InstructionCDFGBitwiseAndNode.class, BitWiseAndExpression.class);
        map.put(InstructionCDFGShiftLeftLogicalNode.class, LeftShiftExpression.class);
        map.put(InstructionCDFGMultiplicationNode.class, MultiplicationExpression.class);
        
        
        MAP = Collections.unmodifiableMap(map);
    }
}

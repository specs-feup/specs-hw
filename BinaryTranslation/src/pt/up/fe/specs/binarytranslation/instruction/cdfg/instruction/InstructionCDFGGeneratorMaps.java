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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.edge.modifiers.GenericCDFGBitSelectModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.edge.modifiers.GenericCDFGModifierGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.edge.modifiers.GenericCDFGSignedModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.edge.modifiers.GenericCDFGUnsignedModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.GenericCDFGNodeGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGAdditionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGDivisionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGMultiplicationNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGShiftLeftLogicalNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGShiftRightArithmeticNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGShiftRightLogicalNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGSubtractionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.bitwise.GenericCDFGAssignNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.bitwise.GenericCDFGBitwiseAndNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.bitwise.GenericCDFGBitwiseNotNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.bitwise.GenericCDFGBitwiseOrNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.bitwise.GenericCDFGBitwiseXorNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGGreaterThanNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGGreaterThanOrEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGLessThanNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGLessThanOrEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGNotEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.logical.GenericCDFGLogicalAndNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.logical.GenericCDFGLogicalNotNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.logical.GenericCDFGLogicalOrNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.function.GenericCDFGSignExtendNode;

public class InstructionCDFGGeneratorMaps {

    public static final Map<String, Supplier<GenericCDFGNodeGenerator>> OPERATION_MAP;
    public static final Map<String, Supplier<GenericCDFGNodeGenerator>> FUNCTION_MAP;
    public static final Map<String, Supplier<GenericCDFGModifierGenerator>> MODIFIER_MAP;
    
    static {
        var amap = new HashMap<String,  Supplier<GenericCDFGNodeGenerator>>();
        
        //Expressions
         
        //Arithmetic Nodes
        amap.put("+", GenericCDFGAdditionNode::new);
        amap.put("-", GenericCDFGSubtractionNode::new);
        amap.put("*", GenericCDFGMultiplicationNode::new);
        amap.put("/", GenericCDFGDivisionNode::new);
       // amap.put("sqrt", InstructionCDFGSquareRoot::new);
        amap.put("<<", GenericCDFGShiftLeftLogicalNode::new);
        amap.put(">>", GenericCDFGShiftRightLogicalNode::new);
        amap.put(">>>", GenericCDFGShiftRightArithmeticNode::new);
        
        //Bitwise Nodes
       amap.put("&", GenericCDFGBitwiseAndNode::new);
       amap.put("^", GenericCDFGBitwiseXorNode::new);
       amap.put("|", GenericCDFGBitwiseOrNode::new);
       amap.put("~", GenericCDFGBitwiseNotNode::new);
       
       //Comparison Nodes
       amap.put("==", GenericCDFGEqualsToNode::new);
       amap.put("!=", GenericCDFGNotEqualsToNode::new);
       amap.put(">", GenericCDFGGreaterThanNode::new);
       amap.put(">=", GenericCDFGGreaterThanOrEqualsToNode::new);
       amap.put("<", GenericCDFGLessThanNode::new);
       amap.put("<=", GenericCDFGLessThanOrEqualsToNode::new);
       
       //Logical Nodes
       amap.put("&&", GenericCDFGLogicalAndNode::new);
       amap.put("||", GenericCDFGLogicalOrNode::new);
       amap.put("!", GenericCDFGLogicalNotNode::new);
       
       //Data
       
       //TODO: add assignment node
 
       amap.put("=", GenericCDFGAssignNode::new);
       
        OPERATION_MAP = Collections.unmodifiableMap(amap);
    }
    
    static {
        var amap = new HashMap<String,  Supplier<GenericCDFGNodeGenerator>>();
      
       //Functions
       
       amap.put("sext", GenericCDFGSignExtendNode::new);

       
        FUNCTION_MAP = Collections.unmodifiableMap(amap);
    }
    
    static {
        var amap = new HashMap<String,  Supplier<GenericCDFGModifierGenerator>>();
      
       //Functions
       
       amap.put("unsigned", GenericCDFGUnsignedModifier::new);
       amap.put("signed", GenericCDFGSignedModifier::new);
      // amap.put("unsigned", GenericCDFGBitSelectModifier::new);
      // amap.put("unsigned", GenericCDFGBitSelectModifier::new);
       
        MODIFIER_MAP = Collections.unmodifiableMap(amap);
    }
    
}

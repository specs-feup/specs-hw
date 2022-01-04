/**
 *  Copyright 2022 SPeCS.
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

package pt.up.fe.specs.binarytranslation.hardware.generation.visitors.wip;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.AInstructionCDFGModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGRangeSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGScalarSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.operand.AInstructionCDFGOperandEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.visitor.wip.InstructionCDFGSubgraphVisitor;
import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;
import pt.up.fe.specs.crispy.ast.expression.operator.subscript.RangedSubscript;
import pt.up.fe.specs.crispy.ast.expression.operator.subscript.ScalarSubscript;

public class InstructionCDFGSubgraphConverter extends InstructionCDFGSubgraphVisitor<HardwareNode>{

    protected final HardwareModule module;
    
    protected InstructionCDFGSubgraphConverter(AInstructionCDFGSubgraph subgraph, HardwareModule module) {
        super(subgraph);

        this.module = module;
        
        subgraph.getInputs().stream().filter(input -> (this.module.getWire(input.getUID()) != null)).forEach(input -> this.module.addWire(input.getUID(), 32));
    
    }

    protected List<HardwareExpression> getOperationOperandsSignals(AInstructionCDFGNode vertex){
        
        List<HardwareExpression> operandSignals = new ArrayList<>();
        
        this.subgraph.getVerticesBefore(vertex).forEach(vertexBefore -> {
            
            Wire operandSignal = (Wire) this.module.getWire(vertexBefore.getUID());

           for(AInstructionCDFGModifier modifier : ((AInstructionCDFGOperandEdge)this.subgraph.getEdge(vertex, vertexBefore)).getModifiers()){
                if(modifier instanceof InstructionCDFGRangeSubscript) {
                    operandSignal = (Wire) operandSignal.idx(((InstructionCDFGRangeSubscript)modifier).getUpperBound(), ((InstructionCDFGRangeSubscript)modifier).getLowerBound());
                }else if(modifier instanceof InstructionCDFGScalarSubscript) {
                    operandSignal = (Wire) operandSignal.idx(((InstructionCDFGScalarSubscript)modifier).getUpperBound());
                }
            }
            
            operandSignals.add(operandSignal);
            
            });
        
        return operandSignals;
    }
}
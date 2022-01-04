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

import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareNodeExpressionMap;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.AInstructionCDFGModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGRangeSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGScalarSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.operand.AInstructionCDFGOperandEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.AInstructionCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;
import pt.up.fe.specs.crispy.ast.statement.ProceduralBlockingStatement;

public class InstructionCDFGDataFlowSubgraphConverter extends InstructionCDFGSubgraphConverter{
    
    public InstructionCDFGDataFlowSubgraphConverter(AInstructionCDFGSubgraph subgraph, HardwareModule module) {
        super(subgraph, module);
        
        this.subgraph.getOutputs().stream().filter(output -> (this.module.getWire(output.getUID()) != null)).forEach(output -> this.module.addWire(output.getUID(), 32));
        
    }
    
    public static void convert(AInstructionCDFGSubgraph subgraph, HardwareModule module, HardwareNode parentHardwareNode) {
        
        InstructionCDFGDataFlowSubgraphConverter converter = new InstructionCDFGDataFlowSubgraphConverter(subgraph, module);
        
        converter.begin(parentHardwareNode);
        
    }
    
    private Wire getOperationSignal(AInstructionCDFGNode vertex) {
        
        Wire operationSignal;
        
        for(AInstructionCDFGNode nextVertex : this.subgraph.getVerticesAfter(vertex)) {
            
            if(nextVertex instanceof AInstructionCDFGDataNode) {
                
                operationSignal = (Wire) this.module.getWire(nextVertex.getUID());

                for(AInstructionCDFGModifier modifier : ((AInstructionCDFGOperandEdge)this.subgraph.getEdge(vertex, nextVertex)).getModifiers()){
                     if(modifier instanceof InstructionCDFGRangeSubscript) {
                         return (Wire) operationSignal.idx(((InstructionCDFGRangeSubscript)modifier).getUpperBound(), ((InstructionCDFGRangeSubscript)modifier).getLowerBound());
                     }else if(modifier instanceof InstructionCDFGScalarSubscript) {
                         return (Wire) operationSignal.idx(((InstructionCDFGScalarSubscript)modifier).getUpperBound());
                     }
                 }

                return (Wire) operationSignal;
            }
            
        }
        
        return this.module.addWire(vertex.getUID(), 32);
    }

    
    
    @Override
    protected void visitOperationVertex(AInstructionCDFGNode vertex, HardwareNode parentHardwareNode) {
        
        Wire operationSignal = this.getOperationSignal(vertex);
        
        HardwareExpression operationExpression = HardwareNodeExpressionMap.generate(vertex.getClass(), this.getOperationOperandsSignals(vertex));
        
        parentHardwareNode.addChild(new ProceduralBlockingStatement(operationSignal, operationExpression));
        
        super.visitOperationVertex(vertex, parentHardwareNode);
    }
    
}
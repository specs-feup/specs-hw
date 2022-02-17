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
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.AInstructionCDFGModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGRangeSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGScalarSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.AInstructionCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGAssignmentNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.statement.ProceduralBlockingStatement;

/** Converts a InstructionCDFGDataFlowSubgraph into a Cripsy AST
 * 
 * @author Joao Conceicao
 *
 */

public class InstructionCDFGDataFlowSubgraphConverter extends InstructionCDFGSubgraphConverter{
    
    public InstructionCDFGDataFlowSubgraphConverter(AInstructionCDFGSubgraph subgraph, HardwareModule module) {
        super(subgraph, module);
        
        this.subgraph.getOutputs().stream()
            .filter(output -> (this.module.getDeclaration(output.getUID()) == null))
            .forEach(output -> this.module.addRegister(output.getUID(), 32));   // generates all of the output wires, if they were not already present in the module
        
    }
    
    /** Converts the subgraph to a Cripsy AST and adds it to the parentHardwareNode
     * 
     * @param subgraph InstructionCDFGDataFlowSubgraph to be converted
     * @param module Top level module
     * @param parentHardwareNode Parent of the generated Cripsy AST
     */
    public static void convert(AInstructionCDFGSubgraph subgraph, HardwareModule module, HardwareNode parentHardwareNode) {
        
        InstructionCDFGDataFlowSubgraphConverter converter = new InstructionCDFGDataFlowSubgraphConverter(subgraph, module);
        
        converter.begin(parentHardwareNode);
        
    }
    
    private VariableOperator getOperationSignal(AInstructionCDFGNode vertex) {
        
        VariableOperator operationSignal;
        
        for(AInstructionCDFGNode nextVertex : this.subgraph.getVerticesAfter(vertex)) {
          
            if(nextVertex instanceof AInstructionCDFGDataNode) {
                
                operationSignal = (VariableOperator) this.module.getDeclaration(nextVertex.getUID());

                AInstructionCDFGEdge operandEdge =  this.subgraph.getEdge(vertex, nextVertex);
                
                for(AInstructionCDFGModifier modifier : operandEdge.getModifiers()){
                     if(modifier instanceof InstructionCDFGRangeSubscript) {
                         return operationSignal.idx(((InstructionCDFGRangeSubscript)modifier).getUpperBound(), ((InstructionCDFGRangeSubscript)modifier).getLowerBound());
                     }else if(modifier instanceof InstructionCDFGScalarSubscript) {
                         return operationSignal.idx(((InstructionCDFGScalarSubscript)modifier).getUpperBound());
                     }
                 }

                return operationSignal;
            }
            
        }
        
        return this.module.addRegister(vertex.getUID(), 32);
    }
    
    @Override
    protected void visitOperationVertex(AInstructionCDFGNode vertex, HardwareNode parentHardwareNode) {
        
        VariableOperator operationSignal = this.getOperationSignal(vertex);
        
        if(vertex instanceof InstructionCDFGAssignmentNode) {
            
            this.getOperationOperandsSignals(vertex).forEach(operand -> parentHardwareNode.addChild(new ProceduralBlockingStatement(operationSignal, operand)));
 
        }else {
            
            HardwareExpression operationExpression = HardwareNodeExpressionMap.generate(vertex.getClass(), this.getOperationOperandsSignals(vertex));
            
            parentHardwareNode.addChild(new ProceduralBlockingStatement(operationSignal, operationExpression));
            
        }
        
        super.visitOperationVertex(vertex, parentHardwareNode);
    }
    
}
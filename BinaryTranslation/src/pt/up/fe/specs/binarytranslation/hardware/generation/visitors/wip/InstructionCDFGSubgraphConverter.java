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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.AInstructionCDFGModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGRangeSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGScalarSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.operand.AInstructionCDFGOperandEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.operand.InstructionCDFGUnaryOperandEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.bitwise.AInstructionCDFGBitwiseOperationNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.visitor.wip.InstructionCDFGSubgraphVisitor;
import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;

/** Generates a Cripsy AST from a AInstructionCDFGSubgraph
 * 
 * @author Jo�o Concei��o
 *
 */
public class InstructionCDFGSubgraphConverter extends InstructionCDFGSubgraphVisitor<HardwareNode>{

    protected final HardwareModule module;
    
    protected InstructionCDFGSubgraphConverter(AInstructionCDFGSubgraph subgraph, HardwareModule module) {
        super(subgraph);

        this.module = module;
        
        subgraph.generateIO();
        
        subgraph.getInputs().stream()
            .filter(input -> !(input instanceof InstructionCDFGLiteralNode))
            .filter(input -> (input.getUIDVal() == 0))
            .filter(input -> (this.module.getDeclaration(input.getUID()) == null))
            .forEach(input -> this.module.addWire(input.getUID(), 32));

        
    }
    
    protected boolean nextVerticesNot(Predicate<AInstructionCDFGNode> notPredicate, AInstructionCDFGNode vertex) {
        return this.subgraph.getVerticesAfter(vertex).stream().anyMatch(notPredicate);
    }
    
    /** Gets a List of the operands Cripsy AST vertices of the operation described by the argument vertex<br>
     * This functions assumes that all nodes that the operation depends on have been resolved previously.  
     * 
     * @param vertex AInstructionCDFGOperationNode to check the operands of
     * @return A List of HardwareExpression (currently Wire) of the operands of the operation decribed by the argument vertex 
     */
    protected List<HardwareExpression> getOperationOperandsSignals(AInstructionCDFGNode vertex){
        
        if(this.subgraph.incomingEdgesOf(vertex).size() == 1) {
            return List.of(this.getOperandHardwareDeclaration((AInstructionCDFGNode)this.subgraph.getVerticesBefore(vertex).toArray()[0], vertex));
        }else {
        
            return List.of(
                    this.getOperandHardwareDeclaration(this.subgraph.getLeftOperand(vertex), vertex), 
                    this.getOperandHardwareDeclaration(this.subgraph.getRightOperand(vertex), vertex)
                    );
        }
    }
    
    protected HardwareExpression getOperandHardwareDeclaration(AInstructionCDFGNode operand, AInstructionCDFGNode operator) {
        
        if(operand instanceof InstructionCDFGLiteralNode) {
            return new Immediate(((InstructionCDFGLiteralNode)operand).getValue(), 32);
        }else {
            
            VariableOperator operandSignal = (VariableOperator) this.module.getDeclaration(operand.getUID());
            
            if((this.subgraph.getEdge(operand, operator) instanceof AInstructionCDFGOperandEdge)) {
       
               for(AInstructionCDFGModifier modifier : ((AInstructionCDFGOperandEdge)this.subgraph.getEdge(operand, operator)).getModifiers()){   
                   
                    if(modifier instanceof InstructionCDFGRangeSubscript) {
                        operandSignal = operandSignal.idx(((InstructionCDFGRangeSubscript)modifier).getUpperBound(), ((InstructionCDFGRangeSubscript)modifier).getLowerBound());
                    }else if(modifier instanceof InstructionCDFGScalarSubscript) {
                        operandSignal = operandSignal.idx(((InstructionCDFGScalarSubscript)modifier).getUpperBound());
                    }
                }
            }

            return operandSignal;
        }
        
    }
}
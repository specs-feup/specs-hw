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

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.visitor.wip.InstructionCDFGVisitor;
import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.statement.IfElseStatement;
import pt.up.fe.specs.crispy.ast.statement.IfStatement;

public class InstructionCDFGConverter extends InstructionCDFGVisitor<HardwareNode>{

    private HardwareModule module;
    
    
    public InstructionCDFGConverter(InstructionCDFG icdfg, HardwareModule module) {
        super(icdfg);
        
        this.module = module;
    }
    
    public static void convert(InstructionCDFG icdfg, HardwareModule module, HardwareNode parentHardwareNode) {
        
        InstructionCDFGConverter converter = new InstructionCDFGConverter(icdfg, module);
        
        converter.begin(parentHardwareNode);
    }

    @Override
    protected void visitDataFlowSubgraph(AInstructionCDFGSubgraph subgraph, HardwareNode parentHardwareNode) {
       
        InstructionCDFGDataFlowSubgraphConverter.convert(subgraph, module, parentHardwareNode);
        
        this.icdfg.getVerticesAfter(subgraph).forEach(subgraphAfter -> this.visit(subgraphAfter, parentHardwareNode));
    }
    
    @Override
    protected void visitControlFlowIfSubgraph(InstructionCDFGControlFlowIf subgraph, HardwareNode parentHardwareNode) {

        IfStatement IfHardwareNode = new IfStatement(InstructionCDFGConditionalSubgraphConverter.convert(subgraph, module));
        
        parentHardwareNode.addChild(IfHardwareNode);
        
        this.visit(this.icdfg.getTruePath(subgraph), IfHardwareNode.then());
        
        this.icdfg.getVerticesAfter(subgraph.getMerge()).forEach(subgraphAfter -> this.visit(subgraphAfter, parentHardwareNode));
        
    }
    
    @Override
    protected void visitControlFlowIfElseSubgraph(InstructionCDFGControlFlowIfElse subgraph, HardwareNode parentHardwareNode) {
        
        IfElseStatement IfElseHardwareNode = new IfElseStatement(InstructionCDFGConditionalSubgraphConverter.convert(subgraph, module));
        
        parentHardwareNode.addChild(IfElseHardwareNode);
        
        this.visit(this.icdfg.getTruePath(subgraph), IfElseHardwareNode.then());
        this.visit(this.icdfg.getFalsePath(subgraph), IfElseHardwareNode.orElse());
        
        this.icdfg.getVerticesAfter(subgraph.getMerge()).forEach(subgraphAfter -> this.visit(subgraphAfter, parentHardwareNode));    
        
        }
        
    @Override
    protected void visitControlFlowMergeSubgraph(InstructionCDFGControlFlowMerge subgraph, HardwareNode parentHardwareNode) {
       return;
    }

}

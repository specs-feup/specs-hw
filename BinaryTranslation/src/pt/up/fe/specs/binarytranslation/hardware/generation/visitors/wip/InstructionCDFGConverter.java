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

package pt.up.fe.specs.binarytranslation.hardware.generation.visitors.wip;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.HardwareStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfElseStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfStatement;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AControlFlowNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.ControlFlowIfElseNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.ControlFlowIfNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.ControlFlowNodeMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFGConverter {

    public HardwareNode convert(InstructionCDFG icdfg, HardwareNode parent) {
        return null;
    }
    
    public HardwareNode visit(InstructionCDFG icdfg, HardwareNode parent, GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> node) {
        
        if(node instanceof InstructionCDFGDataFlowSubgraph) {
            return this.visitDataFlowSubgraph(icdfg, parent, node);
        }
        
        if(node instanceof AControlFlowNode) {
            return this.visitControlFlowNode(icdfg, parent, node);
        }
        
        return null;
    }
    
    public HardwareNode visitDataFlowSubgraph(InstructionCDFG icdfg, HardwareNode parent, GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> node) {
        
    }
    
    public HardwareNode visitControlFlowNode(InstructionCDFG icdfg, HardwareNode parent, GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> node) {
        
        if(node instanceof ControlFlowIfNode) {
            return this.visitControlFlowIfNode(icdfg, parent, node);
        }
        
        if(node instanceof ControlFlowIfElseNode) {
            return this.visitControlFlowIfElseNode(icdfg, parent, node);
        }
        
        if(node instanceof ControlFlowNodeMerge) {
            
        }
        
        return null;
    }
    
    public HardwareNode visitControlFlowIfNode(InstructionCDFG icdfg, HardwareNode parent, GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> node) {
        HardwareStatement statement = new IfStatement((HardwareExpression) this.visitDataFlowSubgraph(node, node_previous));
        
        return statement;
    }
    
    public HardwareNode visitControlFlowIfElseNode(InstructionCDFG icdfg, HardwareNode parent, GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> node) {
        
        HardwareStatement statement = new IfElseStatement((HardwareExpression) this.visitDataFlowSubgraph(node, node_previous));
        
        return statement;
    }
    
    
    
}

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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.visitor.wip;

import java.util.HashSet;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control.AInstructionCDFGControlNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.AInstructionCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.AInstructionCDFGOperationNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;

public class InstructionCDFGSubgraphVisitor<A> {

    protected AInstructionCDFGSubgraph subgraph;
    protected Set<AInstructionCDFGNode> verticesResolved;
    
    public InstructionCDFGSubgraphVisitor(AInstructionCDFGSubgraph subgraph) {
        
        this.subgraph = subgraph;
        this.verticesResolved = new HashSet<>();
        
    }
    
    public void begin(A initialVisitArgument) {
        this.subgraph.getInputs().forEach(input -> this.visit(input, initialVisitArgument));
    }
    
    public void begin() {
        this.subgraph.getInputs().forEach(input -> this.visit(input, null));
    }
    
    protected boolean canVisit(AInstructionCDFGNode vertex) {
        return (this.subgraph.getVerticesBefore(vertex).size() == 0) ? true : !this.subgraph.getVerticesBefore(vertex).stream().anyMatch(vertexBefore -> !this.verticesResolved.contains(vertexBefore));
    }
    
    protected void visit(AInstructionCDFGNode vertex, A visitArgument) {

        if(this.canVisit(vertex)) {
            
            this.verticesResolved.add(vertex);
            
            if(vertex instanceof AInstructionCDFGDataNode) {
                this.visitDataVertex(vertex, visitArgument);
            }else if(vertex instanceof AInstructionCDFGOperationNode) {
                this.visitOperationVertex(vertex, visitArgument);
            }else if(vertex instanceof AInstructionCDFGControlNode) {
                this.visitControlVertex(vertex, visitArgument);
            }else {
                throw new IllegalArgumentException(vertex.getClass().toString() + " is an invalid vertex to visit!");
            }
            
        }
        
    }
    
    protected void visitDataVertex(AInstructionCDFGNode vertex, A visitArgument) {
        this.subgraph.getVerticesAfter(vertex).forEach(vertexAfter -> this.visit(vertexAfter, visitArgument));
    }
    
    protected void visitOperationVertex(AInstructionCDFGNode vertex, A visitArgument) {
        this.subgraph.getVerticesAfter(vertex).forEach(vertexAfter -> this.visit(vertexAfter, visitArgument));
    }
    
    protected void visitControlVertex(AInstructionCDFGNode vertex, A visitArgument) {
        this.subgraph.getVerticesAfter(vertex).forEach(vertexAfter -> this.visit(vertexAfter, visitArgument));
    }
}

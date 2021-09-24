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

package pt.up.fe.specs.binarytranslation.instruction.dfg;

import java.util.List;
import java.util.Map;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.InstructionDFGNode;

public  class InstructionDFG extends Multigraph<InstructionDFGNode, DefaultEdge> {
    
    private List<InstructionDFGNode> inputs;
    private List<InstructionDFGNode> outputs;
    
    private Map<String, InstructionDFGNode> reg_last_use; // For merging dfg generated from instructions with the main dfg 
    
    /*
    private static GraphType GTYPE = GraphTypeBuilder
            .<InstructionDFGNode, DefaultWeightedEdge> directed()
            .allowingMultipleEdges(true)
            .allowingSelfLoops(false)
            .edgeClass(DefaultWeightedEdge.class).buildType();
   */
    
    
    public InstructionDFG() {
        
        super(DefaultEdge.class);
                
        
        this.vertexSet()
        
        //super(null, SupplierUtil.createSupplier(DefaultWeightedEdge.class), GTYPE);
        //var pseudcode = inst.getPseudocode().toString()
    }
     
 
    
}

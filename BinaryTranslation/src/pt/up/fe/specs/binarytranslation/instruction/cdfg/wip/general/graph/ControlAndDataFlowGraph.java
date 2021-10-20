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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph;

import java.util.List;

import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraph;

public class ControlAndDataFlowGraph<V, DFG extends V, CFN extends V, E> extends GeneralFlowGraph<V, E>{

    
    private final Class<? extends DFG> DataFlowGraphClass;
    private final Class<? extends CFN> ControlFlowNodeClass;
    
    public ControlAndDataFlowGraph(Class<? extends DFG> DataFlowGraphClass, Class<? extends CFN> ControlFlowNodeClass, Class<? extends E> EdgeClass) {
        
        super(EdgeClass);
        
        this.ControlFlowNodeClass = ControlFlowNodeClass;
        this.DataFlowGraphClass = DataFlowGraphClass;
        
    }

    @Override
    public void addIncomingEdgesTo(V target, List<V> sources){      
        sources.forEach((source) -> {this.addEdge(source, target);});
    }
    
    @Override
    public void addOutgoingEdgesTo(V source, List<V> targets) {     
        targets.forEach((target) -> {this.addEdge(source, target);});
    }
 
    @Override 
    public boolean addVertex(V vertex) throws IllegalArgumentException{
        
        /*
        if((!ControlFlowNodeClass.in(vertex)) && (!this.isDataFlowGraph(vertex))) {
            throw new IllegalArgumentException();
        }
        */
        
        return super.addVertex(vertex);
    }
    
}

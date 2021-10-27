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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.general.controlanddataflowgraph;

import java.util.Set;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;

public class ControlAndDataFlowGraph<V, E> extends GeneralFlowGraph<V, E>{

    private final Class<? extends V> DataFlowGraphClass;
    private final Class<? extends V> ControlFlowNodeClass;
    
    public ControlAndDataFlowGraph(Class<? extends V> DataFlowGraphClass, Class<? extends V> ControlFlowNodeClass, Class<? extends E> EdgeClass) {
        
        super(EdgeClass);
        
        this.ControlFlowNodeClass = ControlFlowNodeClass;
        this.DataFlowGraphClass = DataFlowGraphClass;
        
    }
    
    public void isValidVertex(V vertex) throws IllegalArgumentException{
        if((!DataFlowGraphClass.isInstance(vertex)) && (!ControlFlowNodeClass.isInstance(vertex))) {
            throw new IllegalArgumentException();
        }
    }
    
    @Override
    public boolean addVertex(V vertex) throws IllegalArgumentException{
        
        this.isValidVertex(vertex);

        return super.addVertex(vertex);
    }
    
    public void addControlEdgesTo(V decision, V path_true, V path_false) {
        this.addVerticesBefore(Set.of(path_true, path_false), decision);
    }
    
}

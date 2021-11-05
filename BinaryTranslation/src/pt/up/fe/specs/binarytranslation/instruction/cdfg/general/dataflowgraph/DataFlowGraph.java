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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.general.dataflowgraph;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;

public class DataFlowGraph<V,E> extends GeneralFlowGraph<V,E>{
   
    private final Class<? extends V> DataVertexClass;
    private final Class<? extends V> OperationVertexClass;
    
    public DataFlowGraph(Class<? extends V> DataVertexClass, Class<? extends V> OperationVertexClass, Class<? extends E> EdgeClass) {
        super(EdgeClass);

        this.DataVertexClass = DataVertexClass;
        this.OperationVertexClass = OperationVertexClass;
        
    }
    
    public void isValidVertex(V vertex) throws IllegalArgumentException{
        if((!DataVertexClass.isInstance(vertex)) && (!OperationVertexClass.isInstance(vertex))){
            throw new IllegalArgumentException();
        }
    }
    
    @Override
    public boolean addVertex(V vertex) throws IllegalArgumentException{
        
        this.isValidVertex(vertex);
        
        return super.addVertex(vertex);
    }

    public V getOperand(V operator, Class<? extends E> edge) {
   
        for(E e : this.incomingEdgesOf(operator)) {
            if(edge.isInstance(e)) {
                return this.getEdgeSource(e);
            }
        }
        
        return null;
    }

}

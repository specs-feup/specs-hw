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
import java.util.function.Supplier;

import org.jgrapht.Graphs;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraph;

public class DataFlowGraph<V,E> extends GeneralFlowGraph<V,E>{
   
    private final Class<? extends V> DataVertexClass;
    private final Class<? extends V> OperationVertexClass;
    
    public DataFlowGraph(Class<? extends V> DataVertexClass, Class<? extends V> OperationVertexClass, Class<? extends E> EdgeClass) {
        super(EdgeClass);

        this.DataVertexClass = DataVertexClass;
        this.OperationVertexClass = OperationVertexClass;
        
    }
    
    public boolean isValidVertex(V vertex) {
        return ( (!DataVertexClass.isInstance(vertex)) && (!OperationVertexClass.isInstance(vertex))) ? false : true;
    }
    
    @Override
    public V addInput(V vertex) {
        
        if(!DataVertexClass.isInstance(vertex)) {
            return vertex;
        }
        
        return super.addInput(vertex);
    }
    
    @Override
    public boolean addVertex(V vertex) throws IllegalArgumentException{
        
        if(!this.isValidVertex(vertex)) {
            throw new IllegalArgumentException();
        }
        
        return super.addVertex(vertex);
    }
    
    
    public V addOperation(V operator, V operand_left, V operand_right) {
        
        this.addVertex(operator);
        this.addVertex(operand_left);
        this.addVertex(operand_right);
        this.addEdge(operand_left, operator);
        this.addEdge(operand_right, operator);
        
        return operator;
    }
    
    public V addOperation(V operator, V operand) {
        
        this.addVertex(operator);
        this.addVertex(operand);
        
        this.addEdge(operand, operator);
        
        return operator;
    }
    
  
    

    
}

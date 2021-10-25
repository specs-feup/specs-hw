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

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;

public class ControlFlowNode<V,E> extends GeneralFlowGraph<V,AInstructionCDFGEdge>{
    
    private List<V> control_in;
    private List<V> control_out;
    
    private V vertex;
    
    public ControlFlowNode(V vertex) {
        super(AInstructionCDFGEdge.class);

        
        this.vertex = vertex;
        this.addVertex(vertex);   
 
        
        
        this.control_in = new ArrayList<>();
        this.control_out = new ArrayList<>();
    }
    
    
    
    public V getVertex() {
        return this.vertex;
    }
    
    public void addControlInput(V vertex) {
        this.control_in.add(vertex);
    }
    
    public List<V> getControlInputs(){
        return this.control_in;
    }
    
    public void addControlOutput(V vertex) {
        this.control_out.add(vertex);
    }
    
    public List<V> getControlOutputs(){
        return this.control_out;
    }
   
}

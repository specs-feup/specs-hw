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

package pt.up.fe.specs.binarytranslation.instruction.dfg.instruction;

import java.util.List;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.InstructionDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.data.IntructionDFGRegisterNode;

public class InstructionDFGInstruction extends Multigraph<InstructionDFGNode, DefaultEdge>{

    private List<InstructionDFGNode> inputs;
    private List<InstructionDFGNode> outputs;
    private InstructionDFGNode operation;
    
    public InstructionDFGInstruction(List<String> input_registers, List<String> output_registers, String operation) {
        
        super(DefaultEdge.class);
        
    }
    
   // INSTRUCTION METHODS
    
    public void finalize() {
        
        // Creates edges from the input registers nodes to the operation node
        for(InstructionDFGNode in_reg_node : this.inputs) {
            this.addEdge(in_reg_node, this.operation);
        }
        
        // Creates edges from the operation node to the output registers nodes
        for(InstructionDFGNode out_reg_node : this.outputs) {
            this.addEdge(this.operation, out_reg_node);
        }
        
    }
    
   //INPUT REGISTER METHODS
    
    public List<InstructionDFGNode> getInputRegisters(){
        return this.inputs;
    }
    
    public void setInputRegisters(List<String> registers){
        
        // Creates the input registers nodes
        for(String in_reg_name : registers) {
            this.inputs.add(new IntructionDFGRegisterNode(in_reg_name));
        }
    }
    
    //OUTPUT REGISTER METHODS
    
    public List<InstructionDFGNode> getOutputRegisters(){
        return this.outputs;
    }
    
    public void setOutputRegisters(List<String> registers){
        // Creates the output registers nodes
        for(String out_reg_name : registers) {
            this.outputs.add(new IntructionDFGRegisterNode(out_reg_name));
        }
    }
    
    //OPERATION METHODS
    
    public InstructionDFGNode getOperation() {
        return this.operation;
    }
    
    public void setOperation(String operation) {
        //Creates the operation node
        this.operation = new IntructionDFGRegisterNode(operation);
    }
    
}

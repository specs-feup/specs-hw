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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.hardware;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.AGenericCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.AGenericCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.data.AGenericCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.data.GenericCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.AGenericCDFGExpressionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.segment.SegmentCDFG;

/**
 * @author João Conceição
 */

//For quick validation of the generated modules

// http://digitaljs.tilk.eu/#

public class HardwareCDFG extends AGenericCDFG{
    
    private String module_name;
    private SegmentCDFG scdfg;
    private static final String unique_node_str = "m";
    
    
    public HardwareCDFG(String module_name, SegmentCDFG segment_cdfg) { 
        super(unique_node_str);
        this.module_name = module_name;
        this.scdfg = segment_cdfg;
        this.mergeGraph(segment_cdfg);
    }
    
    public void replaceWithHardwareNodes() {
        
      
        
    }
    
    public void removeLiteralFromInputs() {
        
        for(String data : this.getInputs().keySet()) {
            if(this.getInputs().get(data) instanceof GenericCDFGLiteralNode) {
                this.getInputs().remove(data);
            }
        }
        
    }

    public void print() {
        
        PrintWriter writer = new PrintWriter(System.out, true);

        
        String output = new String();
        
        output += HardwareCDFGExpressionBuilder.moduleDeclaration(this);
        output += HardwareCDFGExpressionBuilder.newLine(1);
        
        output += HardwareCDFGExpressionBuilder.IODeclaration(this);
        output += HardwareCDFGExpressionBuilder.newLine(1);
        
        output += HardwareCDFGExpressionBuilder.alwaysComb(this, (List)new ArrayList<AGenericCDFGNode>(this.vertexSet()));
        
        /*
        for(AGenericCDFGNode node : this.vertexSet()) {
            
            if(node instanceof AGenericCDFGExpressionNode) {
                if( ((AGenericCDFGExpressionNode)node).isUnary() ) {
                    output += HardwareCDFGExpressionBuilder.assign(this.outgoingEdge(node), node, this.incomingLeftEdge(node));  
                }else {
                    output += HardwareCDFGExpressionBuilder.assign(this.outgoingEdge(node), this.incomingLeftEdge(node), node, this.incomingRightEdge(node));  
                }
            }
            
        }
        */
        output += ("endmodule\n");
        
        writer.printf(output);
        
    }
  
    public String getModuleName() {
        return this.module_name;
    }
    
}

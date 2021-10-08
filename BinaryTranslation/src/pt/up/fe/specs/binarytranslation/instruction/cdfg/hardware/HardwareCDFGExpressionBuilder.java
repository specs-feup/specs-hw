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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.hardware;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.edge.GenericCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.AGenericCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.data.AGenericCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.AGenericCDFGExpressionNode;

public class HardwareCDFGExpressionBuilder {

    public static String moduleDeclaration(HardwareCDFG hcdfg) {
        
        String declaration = new String();
        
        declaration += ("module " + hcdfg.getModuleName() + " (");
        
        for(AGenericCDFGDataNode input_node : hcdfg.getInputs().values()) {
            
            declaration += input_node.appendToName("_in") + ",";
        }
        
        List<AGenericCDFGDataNode> node_list = new ArrayList<AGenericCDFGDataNode>(hcdfg.getOutputs().values());
        
        for(int i = 0; i < node_list.size() - 1; i++) {
            declaration +=(node_list.get(i).appendToName("_out") + ",");
        }
        
        
        declaration +=(node_list.get(node_list.size() - 1).appendToName("_out") + ");\n");
        
        return declaration;
    }
    
    public static String IODeclaration(HardwareCDFG hcdfg) {
        
        String declaration = new String();
        
        for(AGenericCDFGDataNode node : hcdfg.getInputs().values()) {
            declaration += portDeclaration(node, true, true, 31);
        }
        
        declaration += HardwareCDFGExpressionBuilder.newLine(1);
        
        for(AGenericCDFGDataNode node : hcdfg.getOutputs().values()) {
            declaration += portDeclaration(node, false, true, 31);
        }
       
        return declaration;
    }
    
    public static String portDeclaration(AGenericCDFGDataNode node, boolean input, boolean combinational, int ... bounds) {
        
        String declaration = new String();
        
        declaration += (input) ? "input " : "output ";
        declaration += (combinational) ? "wire " : "reg ";
        declaration += (bounds.length > 0) ? ( "[" + String.valueOf(bounds[0]) + ":" + ((bounds.length == 2) ?String.valueOf(bounds[1]) : "0")   +"]") : "";
        declaration += node.getName() + ";\n";
        
        return declaration;
    }
    
    public static String alwaysComb(HardwareCDFG hcdfg, List<AGenericCDFGDataNode> node) {
        
        String block = new String();
        
        block += "always_comb begin\n";
        
        for(AGenericCDFGNode n : node) {
            
            if(n instanceof AGenericCDFGExpressionNode) {
                if( ((AGenericCDFGExpressionNode)n).isUnary() ) {
                    block += "\t" + blockingAssignment(n, n,hcdfg.getEdgeSource(hcdfg.incomingLeftEdge(n)) ); 
                }else {
                    if(hcdfg.getEdgeTarget(hcdfg.outgoingEdge(n)) instanceof AGenericCDFGDataNode) {
                        block += "\t" + blockingAssignment(hcdfg.getEdgeTarget(hcdfg.outgoingEdge(n)),hcdfg.getEdgeSource(hcdfg.incomingLeftEdge(n)), n,hcdfg.getEdgeSource(hcdfg.incomingRightEdge(n)) );  
                    }else {
                        block += "\t" + blockingAssignment(n,hcdfg.getEdgeSource(hcdfg.incomingLeftEdge(n)), n,hcdfg.getEdgeSource(hcdfg.incomingRightEdge(n)) );  
                    }
                        
                }
            }
            
           
        }
        
        block += "end\n";
        
        return block;
    }
    
    public static String blockingAssignment(AGenericCDFGNode output, AGenericCDFGNode left, AGenericCDFGNode operation, AGenericCDFGNode right) { 
        return output.getName() + " = " + left.getName() + " " + operation.toString() + " " + right.getName() + ";\n" ;
    }
    
    public static String blockingAssignment(AGenericCDFGNode output, AGenericCDFGNode operation, AGenericCDFGNode operand) {
        return output.getName() + " = " + operation.getName() + operand.toString() + ";\n" ;
    }
    
    public static String assign(AGenericCDFGNode output, AGenericCDFGNode left, AGenericCDFGNode operation, AGenericCDFGNode right) {
        return "assign " + blockingAssignment(output, left, operation, right) ;
    }
    
    public static String assign(AGenericCDFGNode output, AGenericCDFGNode operation, AGenericCDFGNode operand) {
        return "assign " + blockingAssignment(output, operation, operand) ;
    }

    public static String newLine(int n) {
        
        String line = new String();
        
        for(int i = 0; i < n; i++) {
            line += "\n";
        }
        
        return line;
        
    }
    
}

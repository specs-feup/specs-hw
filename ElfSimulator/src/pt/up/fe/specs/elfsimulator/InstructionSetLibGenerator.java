/**
 *  Copyright 2020 SPeCS.
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

package pt.up.fe.specs.elfsimulator;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.binarytranslation.utils.TreeNode;

public class InstructionSetLibGenerator {
    public InstructionSetLibGenerator(AStaticInstructionStream stream) {
        
            List<Operand> uniqueOperands = new ArrayList<Operand>();
            System.out.print(stream.getNumInstructions() + "\n" );
            int unknownCounter = 0;
            Instruction instruction = stream.nextInstruction();
            while(instruction != null) {
                System.out.print(instruction.getRepresentation() +"\n");
                //System.out.print(instruction.getName() + "\n");
                if(instruction.isUnknown()) 
                    unknownCounter++;
                else {
                    for(Operand operand : instruction.getData().getOperands())
                        if(!uniqueOperands.contains(operand)) uniqueOperands.add(operand);
                    //System.out.print(instruction.getRepresentation() + "\n");
                    //System.out.print("\t" + instruction.getFieldData().getType() + "\n");
                    //System.out.print("\t" + instruction.getFieldData().getFields() + "\n\n");
                        InstructionAST ast = new InstructionAST(instruction);
                        //System.out.println(TreeNode.toString(ast.getRootnode(), "  "));
                        
                        //recursiveNodePrint(ast.getRootnode(), 0);       
                }
                instruction = stream.nextInstruction();
            }
            System.out.print("UNIQUE OPERANDS: ");
            for(Operand operand : uniqueOperands) System.out.print("\t" + operand.getRepresentation() + "\n");
            System.out.print("UNKNOWN INSTRUCTIONS = " + unknownCounter + "\n");
            System.out.print(stream.getNumInstructions() + "\n");
    }
   
    
    
    
    private void recursiveNodePrint(InstructionASTNode node, int indentation) {
        for(int i=0; i<indentation; i++) System.out.print("\t");
        System.out.print(node.getType().name() + "  ->  " + node.getAsString() + "\n");
        for(InstructionASTNode childNode : node.getChildren()) recursiveNodePrint(childNode, ++indentation);
            
    }
    
   
}

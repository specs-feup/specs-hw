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

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class InstructionSimulator {
    
    private Instruction instruction;
    private ProgramState programState;
    
    public InstructionSimulator(Instruction instruction, ProgramState programState) {
        this.instruction = instruction;
        this.programState = programState;
        if(instruction.isUnknown()) System.err.print("Unknown Instruciton");
        else {
            InstructionAST ast = new InstructionAST(instruction);
            resolveAST(ast.getRootnode()); 
        }
    }

    private int resolveAST(InstructionASTNode node) {
        // TODO head recursive function solving non leaf nodes to leaf nodes.
        switch (node.getType()) {
        case PlainStatementNode:
            for(InstructionASTNode childNode : node.getChildren()) resolveAST(childNode);
            return 0;
        case AssignmentExpressionNode:
            InstructionASTNode operand = node.getChild(0);
            this.programState.setRegister(operand.getAsString(), resolveAST(operand.getChild(0)));
            return programState.getRegister(node.getAsString());
        case OperandNode:
            return programState.getRegister(node.getAsString());
        default:
            return -1;
        }
    }
}

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

package pt.up.fe.specs.interpretivesimulator;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;

public class ControlUnit {
    
    Registers registers;
    Ram ram;
    ArithmeticLogicUnit alu;
    
    public ControlUnit(Ram ram, ArithmeticLogicUnit alu, Registers registers) {
        super();
        this.registers = registers;
        this.ram = ram;
        this.alu = alu;
    }
    
    public Number process(Instruction instruction) {
        
        InstructionAST ast = new InstructionAST(instruction);
        resolveAST(ast.getRootnode());
        
        return -1;//no jump exit code
        //TODO: deal with Jumps
    }
    
    private int resolveAST(InstructionASTNode node) {
        // TODO head recursive function solving non leaf nodes to leaf nodes.
        switch (node.getType()) {
        
        case PlainStatementNode:
            for(InstructionASTNode childNode : node.getChildren()) resolveAST(childNode);
            return 0;
            
        case AssignmentExpressionNode:
            String assigned = node.getChild(0).getAsString();
            this.registers.setRegister(assigned, resolveAST(node.getChild(1)));
            return 1;
            
        case OperatorNode:
            alu.processInstruction(resolveAST(node.getChild(0)), resolveAST(node.getChild(1)), node.getAsString());
            return alu.getOutput();
            
        case ImmediateNode:
            return Integer.parseInt(node.getAsString());
            
        case VariableNode:
            return registers.getRegister(node.getAsString());
        
        
        default:
            return -1;
    }
}
    
}

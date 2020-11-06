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

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.AssignmentExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.IfElseStatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.IfStatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ImmediateOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.VariableOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.InstructionASTVisitor;

public class ResolveAST extends InstructionASTVisitor<Integer>{

    final private Registers registers;
    
    public ResolveAST(Registers registers, InstructionASTNode rootNode) throws Exception {
        super();
        this.registers = registers;
        this.visit(rootNode);
        // TODO Auto-generated constructor stub
    }
    
    

    @Override
    public Integer visit(InstructionAST ast) throws Exception {
        //System.out.println(ast.getRootnode().getAsString());
        return super.visit(ast);
    }



    @Override
    protected Integer visit(AssignmentExpressionASTNode node) throws Exception {
        // TODO Auto-generated method stub
        String assignedRegisterName = node.getChild(0).getAsString();
        Integer value;
        if((value = this.visit(node.getChild(0))) != null) {
            registers.setRegister(assignedRegisterName, value);
            return 1;
        }
        else //System.out.println("ERROR: AST resolution returning null on assignment");
        return -1;
    }

    @Override
    protected Integer visit(IfStatementASTNode node) throws Exception {
        // TODO Auto-generated method stub
        return super.visit(node);
    }

    @Override
    protected Integer visit(IfElseStatementASTNode node) throws Exception {
        // TODO Auto-generated method stub
        return super.visit(node);
    }

    @Override
    protected Integer visit(BinaryExpressionASTNode node) throws Exception {
        // TODO Auto-generated method stub
        return super.visit(node);
    }

    @Override
    protected Integer visit(VariableOperandASTNode node) throws Exception {
        // TODO Auto-generated method stub
        return super.visit(node);
    }

    @Override
    protected Integer visit(ImmediateOperandASTNode node) throws Exception {
        // TODO Auto-generated method stub
        return super.visit(node);
    }
    
    
}

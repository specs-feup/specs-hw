/**
 * Copyright 2020 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.binarytranslation.hardware.generation.visitors;

import pt.up.fe.specs.binarytranslation.hardware.generation.exception.UnimplementedExpressionException;
import pt.up.fe.specs.binarytranslation.hardware.generation.exception.UnimplementedStatementException;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareAnchorNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareErrorMessage;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.AssignmentExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.IfElseStatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.IfStatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.InstructionASTVisitor;

/**
 * Receives a PseudoInstructionASTNode (top-level node) and converts it to an appropriate Verilog block
 * 
 * @author nuno
 *
 */
public class InstructionASTConverter extends InstructionASTVisitor<HardwareNode> {

    private HardwareNode parent;
    private HardwareNodeType context;

    public InstructionASTConverter() {
        // TODO Auto-generated constructor stub
    }

    public HardwareNode convertInstruction(HardwareNode parent, PseudoInstructionASTNode instNode) {
        this.parent = parent;
        this.context = parent.getType();
        return this.visit(instNode);
    }

    @Override
    protected HardwareNode visit(PseudoInstructionASTNode node) {

        // TODO: if generation configuration has set the clk, rst signals
        // then the blocks have (?) to be always_ff ???
        // IDEA: use meta hardware nodes with no type and use a second pass to fill that type??

        // TODO: Conditions for generation of a an always_comb block:
        // more than 1 statement
        // if only 1 statement, its an if(-else)

        HardwareNode newleaf = null;
        var statements = node.getStatements();

        switch (this.context) {

        // Im at the top-level
        case ModuleDeclaration:
            if (statements.size() > 1)
                newleaf = new AlwaysCombBlock();
            else
                newleaf = new HardwareAnchorNode();
            break;

        // I'm already in an always block
        case AlwaysComb:
        case AlwaysFF:
        default:
            newleaf = new HardwareAnchorNode();
            break;

        }

        // new context
        newleaf.setParent(this.parent);
        this.context = newleaf.getType();

        // all statements
        for (var stat : statements) {
            try {
                newleaf.addChild(this.visit(stat));

            } catch (UnimplementedStatementException e) {
                newleaf.addChild(new HardwareErrorMessage("Couldn't convert this statement: " + e.getMessage()));

            } catch (UnimplementedExpressionException e) {
                newleaf.addChild(new HardwareErrorMessage("Couldn't convert this expression: " + e.getMessage()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return newleaf;
    }

    @Override
    protected HardwareNode visit(IfStatementASTNode node) throws UnimplementedStatementException {

        // var astexpr = node.getCondition();
        // var ifnode = new IfStatement(expr)

        throw new UnimplementedStatementException(node.getAsString());

        // return super.visit(node);
    }

    @Override
    protected HardwareNode visit(IfElseStatementASTNode node) throws UnimplementedStatementException {
        throw new UnimplementedStatementException(node.getAsString());
        // return super.visit(node);
    }

    @Override
    protected HardwareNode visit(AssignmentExpressionASTNode node) throws Exception {

        var generator = new HardwareAssignmentGenerator();
        try {
            if (this.context == HardwareNodeType.ModuleDeclaration)
                return generator.generateAssign(node);

            else if (this.context == HardwareNodeType.AlwaysComb || this.context == HardwareNodeType.AlwaysFF)
                return generator.generateBlocking(node);

            else
                throw new UnimplementedExpressionException(node.getAsString());

        } catch (Exception e) {
            throw e;
        }

        // TODO Auto-generated method stub

        // ISSUE the type of hw assignment to generate depends on the type of hw node parent im IN...

        // TODO: generating blocking or non blocking statements is tricky...
        // every statement in the pseudoinstruction should be blocking, since they are all parts
        // of the same ISA instruction
        // non blocking statements only make sense when executing multiple instructions in parallel...

        // if SSA is applied, can all statements be nonblocking?? i think so...
    }
}

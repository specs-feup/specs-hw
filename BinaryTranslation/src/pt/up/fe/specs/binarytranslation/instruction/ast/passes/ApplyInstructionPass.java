package pt.up.fe.specs.binarytranslation.instruction.ast.passes;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.BareOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ConcreteOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ImmediateOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.VariableOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

/**
 * Given a specific executed instance of the instruction associated with an AST, apply to the AST the concrete operand
 * names and properties, convert some operand nodes into livein liveout nodes,
 * 
 * @author nuno
 *
 */
public class ApplyInstructionPass extends InstructionASTListener {

    /*
     * The AST being walked
     */
    private InstructionAST ast;

    public ApplyInstructionPass() {
    }

    @Override
    public void visit(InstructionAST ast) {
        this.ast = ast;
        this.visit(ast.getRootnode());
    }

    @Override
    protected void visit(BareOperandASTNode node) {

        var parent = node.getParent();
        var instOp = getOperandByAsmField(ast.getInst().getData().getOperands(), node.getOperandValue());

        // make new node type
        ConcreteOperandASTNode newNode = null;

        // TODO liveins and liveouts??

        // register nodes and symbolic immediates
        if (instOp.isRegister() || (instOp.isImmediate() && instOp.isSymbolic())) {
            newNode = new VariableOperandASTNode(instOp);
        }

        // non symbolic immediate (i.e., actual number value)
        else if (instOp.isImmediate() && !instOp.isSymbolic()) {
            newNode = new ImmediateOperandASTNode(instOp);
        }

        parent.replaceChild(node, newNode);
        return;
    }

    private static Operand getOperandByAsmField(List<Operand> ops, String asmFieldName) {
        for (var op : ops) {
            if (op.getAsmField().toString().equals(asmFieldName))
                return op;
        }
        return null;

        // TODO fix null return
    }
}

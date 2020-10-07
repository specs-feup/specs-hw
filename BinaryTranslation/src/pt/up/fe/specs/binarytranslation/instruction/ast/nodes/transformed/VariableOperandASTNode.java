package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class VariableOperandASTNode extends ConcreteOperandASTNode {

    /*
     * Quick hack to simplify SSA...
     */
    private String TransformedOperandName;

    /*
     * Nodes of this type represent operands which could be: 
     * "r6" or "imma", i.e., some fields are immediate value fields, but treated as inputs to module
     */
    public VariableOperandASTNode(Operand op) {
        super(op);
        this.type = InstructionASTNodeType.VariableNode;
        this.TransformedOperandName = op.getRepresentation().replace("<", "").replace(">", "").replace("[", "")
                .replace("]", "");
        // clean symbolic prefix/suffix if any
        // TODO: this needs some serious work....
    }

    @Override
    public String getAsString() {
        return this.TransformedOperandName;
    }

    public void setValue(String svalue) {
        this.TransformedOperandName = svalue;
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new VariableOperandASTNode(this.op);
    }
}

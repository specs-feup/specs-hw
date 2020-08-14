package pt.up.fe.specs.binarytranslation.hardware.generation.visitors;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModulePortDirection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.PortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareAnchorNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.MetaOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.InstructionASTListener;

public class MetaFieldFetcher extends InstructionASTListener {

    HardwareAnchorNode aux = new HardwareAnchorNode();

    public MetaFieldFetcher() {
        // TODO Auto-generated constructor stub
    }

    // TODO: this is only a quick hack!
    public HardwareNode fetchMetaFields(PseudoInstructionASTNode instNode) {

        try {
            this.visit(instNode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return aux;
    }

    @Override
    protected void visit(MetaOperandASTNode node) {
        this.aux.addChild(new PortDeclaration(node.getAsString(), 1, ModulePortDirection.output));
    }
}

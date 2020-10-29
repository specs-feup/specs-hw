package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

/**
 * This block should emit nothing, and serve only as an anchor point to all declarations in a HardwareTree
 * 
 * @author nuno
 *
 */
public class DeclarationBlock extends HardwareNode {

    public DeclarationBlock() {
        super();
        this.type = HardwareNodeType.DeclarationBlock;
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new DeclarationBlock();
    }
}

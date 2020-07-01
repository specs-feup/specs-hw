package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;

/**
 * This block should emit nothing, and serve only as an anchor point to all declarations in a HardwareTree
 * 
 * @author nuno
 *
 */
public class DeclarationBlock extends AHardwareNode implements HardwareNode {

    public DeclarationBlock() {
        super();
    }
}

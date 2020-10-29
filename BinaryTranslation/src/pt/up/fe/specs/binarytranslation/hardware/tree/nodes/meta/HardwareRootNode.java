package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

/**
 * This block should emit nothing, and serve only as a classed root node to a HardwareTree
 * 
 * @author nuno
 *
 */
public class HardwareRootNode extends HardwareNode {

    public HardwareRootNode() {
        super();
        this.type = HardwareNodeType.Root;
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new HardwareRootNode();
    }
}

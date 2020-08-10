package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

/**
 * A type of meta node that emits nothing, only serves to anchor other children to a parent. Useful for return of
 * visitors/listeners.
 * 
 * @author nuno
 *
 */
public class HardwareAnchorNode extends HardwareNode {

    public HardwareAnchorNode() {
        super();
        this.type = HardwareNodeType.Anchor;
    }

    @Override
    public HardwareNodeType getType() {
        return this.getParent().getType();
    }
}

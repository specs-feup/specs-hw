package pt.up.fe.specs.binarytranslation.hardware.tree;

import java.io.OutputStream;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;

public abstract class AHardwareInstance implements HardwareInstance {

    private HardwareNode root;

    public AHardwareInstance(HardwareNode root) {
        this.root = root;

        // var header = new ModuleHeader();
        // root.addChildAt(header, 0);
    }

    @Override
    public void emit(OutputStream os) {
        this.root.emit(os);
    }
}

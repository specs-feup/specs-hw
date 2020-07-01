package pt.up.fe.specs.binarytranslation.hardware.tree;

import java.io.OutputStream;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareRootNode;

public class AHardwareTree implements HardwareTree {

    protected HardwareRootNode root;

    public AHardwareTree() {
        this.root = new HardwareRootNode();
    }

    @Override
    public HardwareRootNode getRoot() {
        return this.root;
    }

    @Override
    public void emit(OutputStream os) {
        this.root.emit(os);
    }
}

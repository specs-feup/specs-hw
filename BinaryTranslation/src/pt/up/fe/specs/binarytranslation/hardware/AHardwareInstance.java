package pt.up.fe.specs.binarytranslation.hardware;

import java.io.OutputStream;

import pt.up.fe.specs.binarytranslation.hardware.tree.HardwareTree;

public abstract class AHardwareInstance implements HardwareInstance {

    // TODO: the HardareInstance should be either a tree itself, or contain a tree, or have single root node with a
    // header child, and the other children are other subtrees (e.g., each is a verilog module with its only
    // declaration block???)

    protected String instancename;
    protected HardwareTree tree;

    public AHardwareInstance(String instancename, HardwareTree tree) {
        this.instancename = instancename;
        this.tree = tree;
    }

    @Override
    public void emit(OutputStream os) {
        this.tree.emit(os);
    }

    @Override
    public HardwareTree getTree() {
        return this.tree;
    }
}

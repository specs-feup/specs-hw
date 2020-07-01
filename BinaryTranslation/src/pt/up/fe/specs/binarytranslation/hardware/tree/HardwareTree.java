package pt.up.fe.specs.binarytranslation.hardware.tree;

import java.io.OutputStream;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareRootNode;

/*
 * A HardwareTree represents the *only the code* of any HardwareInstance
 */
public interface HardwareTree {

    /*
     * 
     */
    public HardwareRootNode getRoot();

    /*
     * 
     */
    public void emit(OutputStream os);
}

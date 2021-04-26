package pt.up.fe.specs.binarytranslation.hardware;

import java.io.OutputStream;

import pt.up.fe.specs.binarytranslation.hardware.tree.HardwareTree;

public interface HardwareInstance {

    /*
     * 
     */
    public void emit(OutputStream os);

    /*
     * 
     */
    public HardwareTree getTree();
}

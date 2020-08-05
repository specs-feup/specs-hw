package pt.up.fe.specs.binarytranslation.hardware.accelerators.cla;

import java.util.List;

import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.hardware.AHardwareInstance;

/**
 * This type of accelerator is only composed of a parameter list to support the synthesis of a legacy CLA design (see
 * https://bitbucket.org/nmcp88/cla-ipcores)
 */
public class CustomizedLoopAccelerator extends AHardwareInstance {

    /*
     * Should represent a Basic Block or Megablock loop
     */
    private BinarySegmentGraph graph;

    /*
     * Modulo reservation table
     */
    private List<List<Integer>> mrt;

    // TODO issue: how to translate specific instructions from any isa to FUs?
    // generate the FU?

    /**
     * Should be called by {@code CustomizedLoopAcceleratorGenerator}. Basic constructor which receives a list of lines
     * of code directly. Most naive method of definition of hardware module.
     * 
     * @param code
     * @return
     */
    protected CustomizedLoopAccelerator(BinarySegmentGraph graph) {
        super(null, null); // quick fix
        this.graph = graph;
    }
}

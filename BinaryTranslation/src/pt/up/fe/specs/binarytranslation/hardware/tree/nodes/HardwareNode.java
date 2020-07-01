package pt.up.fe.specs.binarytranslation.hardware.tree.nodes;

import java.io.OutputStream;

import pt.up.fe.specs.binarytranslation.utils.TreeNode;

public interface HardwareNode extends TreeNode<HardwareNode> {

    // possible methods:
    // printToFile
    // generateArtifacts (e.g., CLA communication routines, injector, other stuff)
    // simulate, or something like generating a Junit with stimuli
    // estimatePerformance
    // generateVivadoScripts --> produces TCL scripts to generate the hardware netlist?

    /*
     * 
     */
    public void emit(OutputStream os);

    /*
     * 
     */
    public String getAsString();

    // possible method: HardwareReport estimateResources(HardwareTarget target)
    // where HardwareTarget has a type, like FPGA etc
    // the underlying implementations call the synthesis tools etc
}

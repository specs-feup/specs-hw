package pt.up.fe.specs.binarytranslation.hardware.accelerators;

import java.io.OutputStream;

public interface HardwareInstance {

    // possible methods:
    // printToFile
    // generateArtifacts (e.g., CLA communication routines, injector, other stuff)
    // simulate, or something like generating a Junit with stimuli
    // estimatePerformance
    // generateVivadoScripts --> produces TCL scripts to generate the hardware netlist?

    public void emit(OutputStream os);

    // possible method: HardwareReport estimateResources(HardwareTarget target)
    // where HardwareTarget has a type, like FPGA etc
    // the underlying implementations call the synthesis tools etc
}

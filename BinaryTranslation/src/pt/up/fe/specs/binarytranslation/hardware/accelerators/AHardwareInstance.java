package pt.up.fe.specs.binarytranslation.hardware.accelerators;

import java.io.*;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.component.*;

public abstract class AHardwareInstance implements HardwareInstance {

    /*
     * Each component should have an "emit" method
     * Components include: includes header, module declaration, body, plain code list, etc (?) 
     */
    private List<HardwareComponent> components;

    public AHardwareInstance(List<HardwareComponent> components) {
        this.components = components;
    }

    // TODO: replace list of strings with more sophisticated
    // representations of the hardware module, like the stuff in "crisp"

    @Override
    public void emit(OutputStream os) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            for (HardwareComponent comp : this.components) {
                bw.write(comp.getAsString() + "\n");
            }
            bw.flush();
            bw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

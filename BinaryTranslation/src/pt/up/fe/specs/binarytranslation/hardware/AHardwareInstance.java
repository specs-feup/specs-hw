package pt.up.fe.specs.binarytranslation.hardware;

import java.io.*;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.component.ModuleHeader;

public abstract class AHardwareInstance implements HardwareInstance {

    protected ModuleHeader header;
    protected List<String> code;

    // List<HardwareComponent> components; // includes header, module declaration, body, etc (?)

    public AHardwareInstance() {

    }

    // TODO: replace list of strings with more sophisticated
    // representations of the hardware module, like the stuff in "crisp"

    @Override
    public void emit(OutputStream os) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            for (String s : code) {
                bw.write(s);
            }
            bw.flush();
            bw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

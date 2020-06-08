package pt.up.fe.specs.binarytranslation.hardware;

import java.io.*;
import java.util.List;

public abstract class AHardwareInstance implements HardwareInstance {

    protected HardwareModuleHeader header;
    protected List<String> code;

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

package pt.up.fe.specs.binarytranslation.hardware;

import java.util.List;

public abstract class AHardwareInstance implements HardwareInstance {

    private List<String> code;

    public AHardwareInstance(List<String> code) {
        this.code = code;
    }
}

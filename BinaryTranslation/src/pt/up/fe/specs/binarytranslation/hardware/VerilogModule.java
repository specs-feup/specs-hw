package pt.up.fe.specs.binarytranslation.hardware;

import java.io.OutputStream;

import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;

public class VerilogModule extends AHardwareInstance implements HardwareInstance {

    private String moduleName;

    public VerilogModule(String instanceName, String moduleName, VerilogModuleTree tree) {
        super(instanceName, tree);
        this.moduleName = moduleName;
    }

    @Override
    public void emit(OutputStream os) {
        this.tree.emit(os);
    }
}

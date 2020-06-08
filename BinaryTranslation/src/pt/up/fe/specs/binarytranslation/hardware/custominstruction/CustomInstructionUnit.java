package pt.up.fe.specs.binarytranslation.hardware.custominstruction;

import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.AHardwareInstance;

public class CustomInstructionUnit extends AHardwareInstance {

    // private List<Port> inputs, outputs;
    // Port is template class??

    // maybe to simplify, all units should be stream type? fifo interface?z

    // private List<FunctionalUnit> units;

    // private List<HardwareRegisters> registers;
    // HardwareRegister is a template?? templated for bitwidth?
    // or is a register of a generic type of hwdata, including crisp structs etc?

    // private int depth;
    // private String unitName; // how to create names?

    // NOTE: the AHardwareInstance class may maintain its List of strings for the code
    // but I can generate that list to emit, using the private fields of
    // each hardwareinstance subtype

    public CustomInstructionUnit(List<String> code) {
        super(code);
    }

    // methods for custominstructionunitgenreation:
    // addRegister
    // addFunctionalUnit
    // etc
    // then call a factory method or something

}

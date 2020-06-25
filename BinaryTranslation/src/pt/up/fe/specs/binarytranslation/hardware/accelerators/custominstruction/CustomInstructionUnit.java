package pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction;

import java.util.List;

import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.hardware.accelerators.AHardwareInstance;
import pt.up.fe.specs.binarytranslation.hardware.component.HardwareComponent;

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

    // TODO: constructor which makes use of all the fields above

    /*
     * Custom Instruction Units represent a single graph implemented as single-cycle operation
     */
    private BinarySegmentGraph graph;

    /**
     * Should be called by {@code CustomInstructionUnitGenerator}. Basic constructor which receives a list of lines of
     * code directly. Most naive method of definition of hardware module.
     * 
     * @param code
     * @return
     */
    protected CustomInstructionUnit(BinarySegmentGraph graph, List<HardwareComponent> components) {
        super(components);
        this.graph = graph;
    }

    // methods for custominstructionunitgenreation:
    // addRegister
    // addFunctionalUnit
    // etc
    // then call a factory method or something

}

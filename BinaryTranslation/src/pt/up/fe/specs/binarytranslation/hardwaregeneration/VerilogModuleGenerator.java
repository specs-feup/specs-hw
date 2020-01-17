package pt.up.fe.specs.binarytranslation.hardwaregeneration;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.hardware.HardwareInstance;
import pt.up.fe.specs.binarytranslation.hardware.VerilogModule;

/**
 * Generates a single dedicated verilog module for a single binary segment
 * 
 * @author nuno
 *
 */
public class VerilogModuleGenerator implements HardwareGenerator {

    public VerilogModuleGenerator() {
        // TODO pass options to constructor
        // like generation hints
    }

    // TODO maybe this module should be an abstract class to, since theres lots of "verlig modules" i can generate
    // either that, or i reverse the relationship between stuff:
    // ....// HardwareGenerator (I)
    // ....// AHardwareGenerator (AC)
    // ......// SingleCycleModule (C) --> could internally resolve the language?
    // ......// PipelinedDataPathModule (C) --> could internally resolve the language?
    // ......// LoopAcceleratorModule (C) --> could internally resolve the language?

    @Override
    public HardwareInstance generateHardware(BinarySegment segment) {

        // all lines of code
        List<String> verilogCode = new ArrayList<String>();

        // put header
        verilogCode.add("/* \n* This module represents"
                + " the following sequence:\n* " + segment.getRepresentation() + "*/\n");

        // TODO add more stuff to header
        /*
        // put declaration
        int uniqueid = segment.getUniqueId();
        String segtype = segment.getSegmentType().toString().toLowerCase();
        
        verilogCode.add("module " + segtype + "_" + uniqueid + ";\n");
        
        // inputs
        for (Operand op : segment.getLiveIns()) {
            verilogCode.add("\tinput [" + op.getProperties().getWidth()
                    + "-1:0" + "] i_" + op.getRepresentation()
                    + ";\n");
        }
        
        // outputs
        for (Operand op : segment.getLiveOuts()) {
            verilogCode.add("\toutput [" + op.getProperties().getWidth()
                    + "-1:0" + "] o_" + op.getRepresentation()
                    + ";\n");
        }
        
        // put body
        // TODO a way to transform generic algorithm erxpressions into code...
        // i might need to transform segmenst into graphs first after all...
        for (Instruction i : segment.getInstructions()) {
            // String line = "assign " +
            // ??
        }
        
        // end module
        verilogCode.add("endmodule; //" + segtype + "_" + uniqueid + "\n\n");
        */
        System.out.print(verilogCode);

        return new VerilogModule(verilogCode);
    }

    @Override
    public HardwareInstance generateHardware(List<BinarySegment> segments) {

        for (BinarySegment seg : segments)
            this.generateHardware(seg);

        return null; // TODO actually return something
    }

}

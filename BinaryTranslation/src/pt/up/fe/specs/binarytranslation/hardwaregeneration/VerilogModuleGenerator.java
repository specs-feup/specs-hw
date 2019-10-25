package pt.up.fe.specs.binarytranslation.hardwaregeneration;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Operand;

/**
 * Generates a single dedicated verilog module for a single binary segment
 * 
 * @author nuno
 *
 */
public class VerilogModuleGenerator implements HardwareGenerator {

    // all lines of code
    private List<String> verilogCode;

    public VerilogModuleGenerator() {
        this.verilogCode = new ArrayList<String>();
    }

    @Override
    public HardwareInstance generateHardware(BinarySegment segment) {

        // put header
        verilogCode.add("/* \n* This module represents"
                + " the following sequence:\n* " + segment.getRepresentation() + "*/\n");

        // TODO add more stuff to header

        // put declaration
        int uniqueid = segment.getUniqueId();
        String segtype = segment.getSegmentType().toString().toLowerCase();

        verilogCode.add("module " + segtype + "_" + uniqueid + ";\n");

        // live ins and outs
        for (Operand op : segment.getLiveIns()) {
            verilogCode
                    .add("\tinput [" + op.getProperties().getWidth()
                            + "-1:0" + "] i_" + op.getRepresentation()
                            + ";\n");
        }

        for (Operand op : segment.getLiveOuts()) {
            verilogCode
                    .add("\toutput [" + op.getProperties().getWidth()
                            + "-1:0" + "] o_" + op.getRepresentation()
                            + ";\n");
        }

        // put body

        verilogCode.add("endmodule; //" + segtype + "_" + uniqueid + "\n\n");

        System.out.print(verilogCode);

        return null;
    }

    @Override
    public HardwareInstance generateHardware(List<BinarySegment> segments) {

        for (BinarySegment seg : segments)
            this.generateHardware(seg);

        return null; // TODO actually return something
    }

}

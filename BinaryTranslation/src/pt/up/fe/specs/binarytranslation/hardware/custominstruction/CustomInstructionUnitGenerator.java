package pt.up.fe.specs.binarytranslation.hardware.custominstruction;

import java.util.*;

import pt.up.fe.specs.binarytranslation.graphs.*;
import pt.up.fe.specs.binarytranslation.graphs.edge.*;
import pt.up.fe.specs.binarytranslation.hardware.*;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * Generates a single dedicated verilog module for a single binary segment
 * 
 * @author nuno
 *
 */
public class CustomInstructionUnitGenerator extends AHardwareGenerator {

    // todo pass parameter to constructor of generator?
    // e.g. float enable, max latency, max units, etc?

    public CustomInstructionUnitGenerator() {
        // TODO pass options to constructor
        // like generation hints
    }

    @Override
    public HardwareInstance generateHardware(BinarySegmentGraph graph) {

        // NOTE: graph is necessary instead of segment so we can make 
        // good use of blocking and non blocking statements 
        // in a combinatorial Verilog block, by relying on node levels
        
        // TODO: exception here if graph type is different than a frequent sequence! (static or dynamic)
        
        // all lines of code
        List<String> code = new ArrayList<String>();
        
        // put declaration
        int uniqueid = graph.getSegment().getUniqueId();
        String segtype = graph.getSegment().getSegmentType().toString().toLowerCase();
        code.add("module " + segtype + "_" + uniqueid + ";\n");
        
        // inputs
        int i = 0;
        for (GraphInput gi : graph.getLiveins()) {
            code.add("\tinput [" + gi.getWidth()
                    + "-1:0" + "] i_" + Integer.toString(i) 
                    + ",\t//" + gi.getRepresentation() + "\n");
            i++;
        }
        
        // outputs
        i = 0;
        for (GraphOutput go : graph.getLiveouts()) {
            code.add("\toutput [" + go.getWidth()
            + "-1:0" + "] i_" + Integer.toString(i) 
            + ",\t//" + go.getRepresentation() + "\n");
            i++;
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

        return new CustomInstructionUnit(verilogCode);
    }
}

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

    private static final Map<GraphEdgeType, String> PREFIXNAMES;
    static {
        Map<GraphEdgeType, String> aMap = new LinkedHashMap<GraphEdgeType, String>();
        aMap.put(GraphEdgeType.livein, "in");
        aMap.put(GraphEdgeType.liveout, "out");
        aMap.put(GraphEdgeType.immediate, "imm");
        aMap.put(GraphEdgeType.noderesult, "w");
        aMap.put(GraphEdgeType.control, "en");
        PREFIXNAMES = Collections.unmodifiableMap(aMap);
    }

    @Override
    public HardwareInstance generateHardware(BinarySegmentGraph graph) {

        // TODO: exception here if graph type is different than a frequent sequence! (static or dynamic)

        // NOTE: graph is necessary instead of segment so we can make
        // good use of blocking and non blocking statements
        // in a combinatorial Verilog block, by relying on node levels

        /*
         * Map of counters per edge type
         */
        Map<GraphEdgeType, Integer> counters = new LinkedHashMap<GraphEdgeType, Integer>();
        for (GraphEdgeType type : GraphEdgeType.values()) {
            counters.put(type, 0);
        }

        /*
         * Map to remap names of edges into HW signal names
         */
        Map<GraphEdge, String> renameMap = new LinkedHashMap<GraphEdge, String>();

        for (GraphNode n : graph.getNodes()) {
            for (GraphEdge ed : n.getEdges()) {
                if (!renameMap.containsKey(ed)) {
                    var type = ed.getType();
                    int nr = counters.get(type);
                    renameMap.put(ed, PREFIXNAMES.get(type) + Integer.toString(nr));
                    counters.put(type, nr + 1);
                }
            }
        }

        // all lines of code
        List<String> code = new ArrayList<String>();

        // put declaration
        int uniqueid = graph.getSegment().getUniqueId();
        String segtype = graph.getSegment().getSegmentType().toString().toLowerCase();
        code.add("module " + segtype + "_" + uniqueid + ";\n\n");

        // inputs
        for (GraphInput gi : graph.getLiveins()) {
            code.add("\tinput [" + gi.getWidth()
                    + "-1:0" + "] " + renameMap.get(gi)
                    + ";\t//" + gi.getRepresentation() + "\n");
        }
        code.add("\n");

        // outputs
        for (GraphOutput go : graph.getLiveouts()) {
            code.add("\toutput [" + go.getWidth()
                    + "-1:0" + "] " + renameMap.get(go)
                    + ";\t//" + go.getRepresentation() + "\n");
        }
        code.add("\n");

        // if number of contexts is 1, then imms
        // can be a static value, otherwise they are inputs
        var sz = graph.getSegment().getContexts().size();
        String datatype = (sz == 1) ? "localparam" : "input";
        for (GraphNode n : graph.getNodes()) {
            for (GraphEdge ed : n.getEdges()) {
                if (ed.getType() == GraphEdgeType.immediate) {
                    code.add("\t" + datatype + "[" + go.getWidth()
                            + "-1:0" + "] " + renameMap.get(ed)
                            + ";\t//" + ed.getRepresentation() + "\n");
                }
            }
        }

        // put body
        // TODO a way to transform generic algorithm erxpressions into code...
        // i might need to transform segmenst into graphs first after all...
        // for (Instruction i : segment.getInstructions()) {
        // String line = "assign " +
        // ??
        // }

        // end module
        code.add("endmodule; //" + segtype + "_" + uniqueid + "\n\n");

        return new CustomInstructionUnit(graph, code);
    }
}

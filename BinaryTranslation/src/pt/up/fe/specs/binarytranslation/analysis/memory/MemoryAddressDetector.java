package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class MemoryAddressDetector extends APropertyDetector {
    public MemoryAddressDetector(BinarySegment bb, List<Instruction> insts) {
        super(bb, insts);
    }

    public void printOccurrenceRegisters() {
        for (var o : tracker.getOccurrences()) {
            var regs = o.getRegisters();
            regs.prettyPrint();
            AnalysisUtils.printSeparator(40);
        }
    }

    public ArrayList<Graph<AddressVertex, DefaultEdge>> detectGraphs() {
        var out = new ArrayList<Graph<AddressVertex, DefaultEdge>>();

        for (var i : tracker.getBasicBlock().getInstructions()) {
            if (AnalysisUtils.isLoadStore(i)) {
                var builder = new AddressGraphBuilder(tracker.getBasicBlock().getInstructions(), i);
                var graph = builder.calculateChain();
                out.add(graph);
            }
        }
        return out;
    }

    public static String buildMemoryExpression(Graph<AddressVertex, DefaultEdge> graph, AddressVertex root) {
        var sb = new StringBuilder();

        if (root.getType() == AddressVertexType.REGISTER) {
            sb.append(root.getLabel()).append(" <- mem[");

            var start = AddressVertex.nullVertex;
            for (var edge : graph.edgesOf(root)) {
                var parent = graph.getEdgeSource(edge);
                for (var edge1 : graph.edgesOf(parent)) {
                    start = graph.getEdgeSource(edge1);
                }
            }

            sb.append(buildAddressExpression(graph, start));
            sb.append("]");
        }
        if (root.getType() == AddressVertexType.MEMORY) {
            sb.append("mem[");

            var addrStart = AddressVertex.nullVertex;
            var dataToStore = AddressVertex.nullVertex;
            for (var edge : graph.edgesOf(root)) {
                var parent = graph.getEdgeSource(edge);
                switch (parent.getType()) {
                case OPERATION:
                    addrStart = parent;
                    break;
                case IMMEDIATE:
                case REGISTER:
                    dataToStore = parent;
                    break;
                default:
                    break;
                }
            }

            sb.append(buildAddressExpression(graph, addrStart));
            sb.append("] <- ").append(dataToStore.getLabel());
        }
        return sb.toString();
    }

    private static String buildAddressExpression(Graph<AddressVertex, DefaultEdge> graph, AddressVertex start) {
        return "expression";
    }
}

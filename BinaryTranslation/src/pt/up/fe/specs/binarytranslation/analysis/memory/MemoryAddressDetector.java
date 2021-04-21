package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class MemoryAddressDetector extends APropertyDetector {
    public MemoryAddressDetector(BinarySegment bb, List<Instruction> insts) {
        super(bb, insts);
    }

    public void printOccurrenceRegisters() {
        for (var o : getTracker().getOccurrences()) {
            var regs = o.getRegisters();
            regs.prettyPrint();
            AnalysisUtils.printSeparator(40);
        }
    }

    public ArrayList<Graph<AddressVertex, DefaultEdge>> detectGraphs() {
        var out = new ArrayList<Graph<AddressVertex, DefaultEdge>>();

        for (var i : getTracker().getBasicBlock().getInstructions()) {
            if (AnalysisUtils.isLoadStore(i)) {
                var builder = new AddressGraphBuilder(getTracker().getBasicBlock().getInstructions(), i);
                var graph = builder.calculateChain();
                
                //Convert shifts to mults
                var trans = new TransformShiftsToMult(graph);
                trans.applyToGraph();
                
                out.add(graph);
            }
        }
        return out;
    }
    
    public static String buildMemoryExpression(Graph<AddressVertex, DefaultEdge> graph) {
        var root = GraphUtils.findGraphRoot(graph);
        return buildMemoryExpression(graph, root);
    }

    public static String buildMemoryExpression(Graph<AddressVertex, DefaultEdge> graph, AddressVertex root) {
        var sb = new StringBuilder();

        if (root.getType() == AddressVertexType.REGISTER) {
            sb.append(root.getLabel()).append(" <- mem[");

            var start = GraphUtils.getParents(graph, GraphUtils.getParents(graph, root).get(0)).get(0);

            sb.append(buildAddressExpression(graph, start, true));
            sb.append("]");
        }
        if (root.getType() == AddressVertexType.MEMORY) {
            sb.append("mem[");

            var addrStart = AddressVertex.nullVertex;
            var dataToStore = AddressVertex.nullVertex;
            var parents = GraphUtils.getParents(graph, root);

            for (var parent : parents) {
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

            sb.append(buildAddressExpression(graph, addrStart, true));
            sb.append("] <- ").append(dataToStore.getLabel());
        }
        return sb.toString();
    }

    private static String buildAddressExpression(Graph<AddressVertex, DefaultEdge> graph, AddressVertex current, boolean first) {

        if (current.getType() == AddressVertexType.IMMEDIATE) {
            return current.getLabel();
        }
        if (current.getType() == AddressVertexType.REGISTER) {
            var parents = GraphUtils.getParents(graph, current);
            if (parents.size() == 0)
                return current.getLabel();
            else
                return buildAddressExpression(graph, parents.get(0), false);
        }
        if (current.getType() == AddressVertexType.OPERATION) {
            var parents = GraphUtils.getParents(graph, current);
            String s1 = "";
            String s2 = "";
            String s3 = "";
            
            if (parents.size() == 2) {
                var op1 = parents.get(0);
                var op2 = parents.get(1);
                
                s1 = buildAddressExpression(graph, op1, false);
                s2 = current.getLabel();
                s3 = buildAddressExpression(graph, op2, false);
            }
            if (parents.size() == 1) {
                var op1 = parents.get(0);
                s1 = buildAddressExpression(graph, op1, false); 
                
                s2 = "?";
                if (current.getLabel().equals("+")) {
                    s2 = "*";
                }
                if (current.getLabel().equals("*")) {
                    s2 = "^";
                }
                s3 = "2";
            }
            
            if (first)
                return s1 + " " + s2 + " " + s3;
            else
                return "(" + s1 + " " + s2 + " " + s3 + ")";
        }
        return "";
    }
}

package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class PartialAddressGraphBuilder {
    private String startReg;
    private AddressVertex root;
    private int idx;
    private List<Instruction> basicBlock;
    private Graph<AddressVertex, DefaultEdge> graph;

    public PartialAddressGraphBuilder(String reg, int idx, List<Instruction> basicBlock, String prefix) {
        this.startReg = reg;
        this.idx = idx;
        this.basicBlock = basicBlock;
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

    public Graph<AddressVertex, DefaultEdge> generateGraph() {
        // ensure idempotence
        if (graph.vertexSet().size() != 0)
            return graph;

        this.root = new AddressVertex(startReg, AddressVertexType.REGISTER);
        graph.addVertex(root);

        var parent = iterate(startReg, idx);
        if (parent != AddressVertex.nullVertex) {
            graph.addEdge(parent, root);
        }
        return graph;
    }

    private AddressVertex iterate(String register, int index) {
        var ret = AddressVertex.nullVertex;
        boolean exit = false;
        
        for (int i = index; i >= 0; i--) {
            var inst = this.basicBlock.get(i);
            var ops = inst.getData().getOperands();

            if (ops.size() != 0 && ops.get(0).isRegister()) {
                String reg0 = AnalysisUtils.getRegName(ops.get(0));

                if (reg0.equals(register)) {
                    exit = true;
                    
                    // Build operation node
                    String opName = inst.getData().getPlainName();
                    // TODO: map operation name to enum operand
                    var opNode = new AddressVertex(opName, AddressVertexType.OPERATION);
                    graph.addVertex(opNode);
                    ret = opNode;

                    // Handle first operand
                    var op1 = ops.get(1);
                    var previous = AddressVertex.nullVertex;

                    if (op1.isRegister()) {
                        // Build vertex
                        String reg1 = AnalysisUtils.getRegName(op1);
                        var reg1Node = new AddressVertex(reg1, AddressVertexType.REGISTER);
                        previous = reg1Node;

                        // Add vertex and connect to operation
                        graph.addVertex(reg1Node);
                        graph.addEdge(reg1Node, opNode);

                        // Build chain upwards, if it exists at all
                        var parent = iterate(reg1, i - 1);
                        if (parent != AddressVertex.nullVertex) {
                            graph.addEdge(parent, reg1Node);
                        }
                    }

                    // Handle second operand
                    var op2 = ops.get(2);

                    if (op2.isRegister()) {
                        // Build vertex
                        String reg2 = AnalysisUtils.getRegName(op2);
                        var reg2Node = new AddressVertex(reg2, AddressVertexType.REGISTER);

                        // Special case: op1 and op2 are the same register
                        if (previous.getLabel().equals(reg2)) {
                            graph.addEdge(previous, opNode);
                        } else {
                            // Add vertex and connect to operation
                            graph.addVertex(reg2Node);
                            graph.addEdge(reg2Node, opNode);

                            // Build chain upwards, if it exists at all
                            var parent = iterate(reg2, i - 1);
                            if (parent != AddressVertex.nullVertex) {
                                graph.addEdge(parent, reg2Node);
                            }
                        }
                    }
                    if (op2.isImmediate()) {
                        String imm = op2.getRepresentation();
                        var immNode = new AddressVertex(imm, AddressVertexType.IMMEDIATE);
                        graph.addVertex(immNode);
                        graph.addEdge(immNode, opNode);
                    }
                }
                if (exit)
                    return ret;
            }
        }
        return ret;
    }
    
    public String getOperationSymbol(Instruction i) {
        return "";
    }

    public String getStartReg() {
        return startReg;
    }

    public AddressVertex getRoot() {
        return root;
    }
}

package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class PartialAddressGraphBuilder {
    private String startReg;
    private AddressVertex root;
    private Graph<AddressVertex, DefaultEdge> graph;
    private List<Instruction> modifiedBasicBlock;
    private int modifiedIdx;

    public PartialAddressGraphBuilder(String reg, int idx, List<Instruction> basicBlock, String prefix) {
        this.startReg = reg;
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        
        //Basic block needs to undergo a small transformation to consider insts after the jump
        this.modifiedBasicBlock = getModifiedSequence(basicBlock);
        this.modifiedIdx = getModifiedIndex(idx, basicBlock, modifiedBasicBlock);
    }

    private int getModifiedIndex(int idx, List<Instruction> bb, List<Instruction> mbb) {
        var inst = bb.get(idx);
        for (int i = 0; i < mbb.size(); i++) {
            if (mbb.get(i) == inst)
                return i;
        }
        return -1;
    }

    private List<Instruction> getModifiedSequence(List<Instruction> bb) {
        var stack = new Stack<Instruction>();
        for (int i = bb.size() - 1; i >= 0; i--) {
            var inst = bb.get(i);
            if (inst.isJump())
                break;
            else
                stack.add(inst);
        }
        var prolog = new ArrayList<Instruction>(stack);
        List<Instruction> res = new ArrayList<>();
        Stream.of(prolog, bb).forEach(res::addAll);
        return res;
    }

    public Graph<AddressVertex, DefaultEdge> generateGraph() {
        // ensure idempotence
        if (graph.vertexSet().size() != 0)
            return graph;

        this.root = new AddressVertex(startReg, AddressVertexType.REGISTER);
        graph.addVertex(root);

        var parent = iterate(startReg, modifiedIdx);
        if (parent != AddressVertex.nullVertex) {
            graph.addEdge(parent, root);
        }
        return graph;
    }

    private AddressVertex iterate(String register, int index) {
        var ret = AddressVertex.nullVertex;
        boolean exit = false;

        for (int i = index; i >= 0; i--) {
            var inst = this.modifiedBasicBlock.get(i);
            var ops = inst.getData().getOperands();

            if (ops.size() != 0 && ops.get(0).isRegister()) {
                String reg0 = AnalysisUtils.getRegName(ops.get(0));

                if (reg0.equals(register)) {
                    exit = true;

                    // Build operation node
                    // TODO: map other enums to symbols (is there already a way to do this?)
                    String enumName = inst.getProperties().getEnumName();
                    String opName = AnalysisUtils.mapEnum(enumName);

                    var opNode = new AddressVertex(opName, AddressVertexType.OPERATION);
                    graph.addVertex(opNode);
                    ret = opNode;

                    // Special case: op is another load/store
                    // in this case, stop expanding
                    if (AnalysisUtils.isLoadStore(inst))
                        break;

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

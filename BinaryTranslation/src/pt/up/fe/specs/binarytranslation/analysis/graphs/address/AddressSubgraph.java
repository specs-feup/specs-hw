package pt.up.fe.specs.binarytranslation.analysis.graphs.address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class AddressSubgraph extends SimpleDirectedGraph<BtfVertex, DefaultEdge> {
    private static final long serialVersionUID = 7667384967359919218L;
    private String startReg;
    private BtfVertex root;
    //private Graph<BtfVertex, DefaultEdge> graph;
    private List<Instruction> modifiedBasicBlock;
    private int modifiedIdx;

    public AddressSubgraph(String reg, int idx, List<Instruction> basicBlock) {
        super(DefaultEdge.class);
        this.startReg = reg;

        // Basic block needs to undergo a small transformation to consider insts after the jump
        this.modifiedBasicBlock = getModifiedSequence(basicBlock);
        this.modifiedIdx = getModifiedIndex(idx, basicBlock, modifiedBasicBlock);
        
        generateGraph();
    }

    private int getModifiedIndex(int idx, List<Instruction> bb, List<Instruction> mbb) {
        if (idx != -1) {
            var inst = bb.get(idx);
            for (int i = 0; i < mbb.size(); i++) {
                if (mbb.get(i) == inst)
                    return i;
            }
        } 
        else {
            var inst = bb.get(0);
            for (int i = 0; i < mbb.size(); i++) {
                if (mbb.get(i) == inst)
                    return i - 1;
            }
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

    public void generateGraph() {
        this.root = new BtfVertex(startReg, BtfVertexType.REGISTER);
        addVertex(root);

        var parent = iterate(startReg, modifiedIdx);
        if (parent != BtfVertex.nullVertex) {
            addEdge(parent, root);
        }
    }

    private BtfVertex iterate(String register, int index) {
        var ret = BtfVertex.nullVertex;
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
                    String opName = AnalysisUtils.mapInstructionsToSymbol(enumName);

                    var opNode = new BtfVertex(opName, BtfVertexType.OPERATION);
                    addVertex(opNode);
                    ret = opNode;

                    // Special case: op is another load/store
                    // in this case, stop expanding
                    if (AnalysisUtils.isLoadStore(inst))
                        break;

                    // Handle first operand
                    var op1 = ops.get(1);
                    var previous = BtfVertex.nullVertex;

                    if (op1.isRegister()) {
                        // Build vertex
                        String reg1 = AnalysisUtils.getRegName(op1);
                        var reg1Node = new BtfVertex(reg1, BtfVertexType.REGISTER);
                        previous = reg1Node;

                        // Add vertex and connect to operation
                        addVertex(reg1Node);
                        addEdge(reg1Node, opNode);

                        // Build chain upwards, if it exists at all
                        var parent = iterate(reg1, i - 1);
                        if (parent != BtfVertex.nullVertex) {
                            addEdge(parent, reg1Node);
                        }
                    }

                    // Handle second operand
                    var op2 = ops.get(2);

                    if (op2.isRegister()) {
                        // Build vertex
                        String reg2 = AnalysisUtils.getRegName(op2);
                        var reg2Node = new BtfVertex(reg2, BtfVertexType.REGISTER);

                        // Special case: op1 and op2 are the same register
                        if (previous.getLabel().equals(reg2)) {
                            addEdge(previous, opNode);
                        } else {
                            // Add vertex and connect to operation
                            addVertex(reg2Node);
                            addEdge(reg2Node, opNode);

                            // Build chain upwards, if it exists at all
                            var parent = iterate(reg2, i - 1);
                            if (parent != BtfVertex.nullVertex) {
                                addEdge(parent, reg2Node);
                            }
                        }
                    }
                    if (op2.isImmediate()) {
                        String imm = op2.getRepresentation();
                        var immNode = new BtfVertex(imm, BtfVertexType.IMMEDIATE);
                        addVertex(immNode);
                        addEdge(immNode, opNode);
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

    public BtfVertex getRoot() {
        return root;
    }
}

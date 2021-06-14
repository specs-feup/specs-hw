package pt.up.fe.specs.binarytranslation.analysis.graphs.address;

import java.util.List;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexIsaInfo;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexProperty;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class AddressGraph extends SimpleDirectedGraph<BtfVertex, DefaultEdge>{
    private static final long serialVersionUID = -7691187221132327853L;
    private List<Instruction> basicBlock;
    private Instruction inst;
    private int startIdx;

    public AddressGraph(List<Instruction> basicBlock, Instruction inst) {
        super(DefaultEdge.class);
        this.basicBlock = basicBlock;
        this.inst = inst;
        this.startIdx = basicBlock.indexOf(inst);
        buildGraph();
    }

    public void buildGraph() {
        if (inst.getData().getOperands().size() == 3) {
            Operand target = inst.getData().getOperands().get(0);
            Operand op1 = inst.getData().getOperands().get(1);
            Operand op2 = inst.getData().getOperands().get(2);

            String targetReg = AnalysisUtils.getRegisterName(target);
            String op1Reg = AnalysisUtils.getRegisterName(op1);
            String op2Reg = AnalysisUtils.getRegisterName(op2);

            // Build memory access node
            var targetV = new BtfVertex(targetReg, BtfVertexType.REGISTER);
            targetV.setIsaInfo(BtfVertexIsaInfo.RD);
            var memAccessV = new BtfVertex(inst.isLoad() ? "Memory Access (load)" : "Memory Access (store)",
                    BtfVertexType.MEMORY);
            var sumV = new BtfVertex("+", BtfVertexType.OPERATION);

            // Build rA subgraph, add it to this graph 
            var rAGraph = new AddressSubgraph(op1Reg, startIdx - 1, basicBlock);
            Graphs.addGraph(this, rAGraph);
            
            // Set rA vertex
            var rAV = rAGraph.getRoot();
            rAV.setIsaInfo(BtfVertexIsaInfo.RA);

            // Add memory access to rA sungraph, and connect
            addVertex(targetV);
            addVertex(memAccessV);
            addVertex(sumV);
            addEdge(rAV, sumV);
            addEdge(sumV, memAccessV);

            // Build rB subgraph, add it to this graph
            if (op2.isImmediate()) {
                String immVal = op2.getRepresentation();
                var immV = new BtfVertex(immVal, BtfVertexType.IMMEDIATE);
                rAV.setProperty(BtfVertexProperty.BASE_ADDR);
                immV.setProperty(BtfVertexProperty.OFFSET);

                addVertex(immV);
                addEdge(immV, sumV);
            } else {
                var rBGraph = new AddressSubgraph(op2Reg, startIdx - 1, basicBlock);
                Graphs.addGraph(this, rBGraph);
                
                // Set rB vertex
                var rBV = rBGraph.getRoot();
                rBV.setIsaInfo(BtfVertexIsaInfo.RB);

                // Set rA and rB using MicroBlaze GCC conventions
                rAV.setProperty(BtfVertexProperty.OFFSET);
                rBV.setProperty(BtfVertexProperty.BASE_ADDR);
                addEdge(rBV, sumV);
            }

            // Connect load/store destination/source register to the rest
            if (inst.isLoad()) {
                addEdge(memAccessV, targetV);
                targetV.setType(BtfVertexType.LOAD_TARGET);
            } else {
                addEdge(targetV, memAccessV);
                targetV.setType(BtfVertexType.STORE_TARGET);
            }
        }
    }
}

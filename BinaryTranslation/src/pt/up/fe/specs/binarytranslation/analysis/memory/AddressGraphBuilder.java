package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexIsaInfo;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexProperty;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class AddressGraphBuilder {

    private List<Instruction> basicBlock;
    private Instruction inst;
    private int startIdx;

    public AddressGraphBuilder(List<Instruction> basicBlock, Instruction inst) {
        this.basicBlock = basicBlock;
        this.inst = inst;
        this.startIdx = basicBlock.indexOf(inst);
    }

    public Graph<AddressVertex, DefaultEdge> calculateChain() {
        if (inst.getData().getOperands().size() == 3) {
            Operand target = inst.getData().getOperands().get(0);
            Operand op1 = inst.getData().getOperands().get(1);
            Operand op2 = inst.getData().getOperands().get(2);    

            String targetReg = AnalysisUtils.getRegName(target);
            String op1Reg = AnalysisUtils.getRegName(op1);
            String op2Reg = AnalysisUtils.getRegName(op2);

            //Build memory access node
            var targetV = new AddressVertex(targetReg, AddressVertexType.REGISTER);
            targetV.setIsaInfo(AddressVertexIsaInfo.RD);
            var memAccessV = new AddressVertex(inst.isLoad() ? "Memory Access (load)" : "Memory Access (store)",
                    AddressVertexType.MEMORY);
            var sumV = new AddressVertex("+", AddressVertexType.OPERATION);

            //Build rA graph
            var rABuilder = new PartialAddressGraphBuilder(op1Reg, startIdx - 1, basicBlock);
            var fullGraph = rABuilder.generateGraph();
            var rAV = rABuilder.getRoot();
            rAV.setIsaInfo(AddressVertexIsaInfo.RA);

            //Add memory access to rA graph, and connect
            fullGraph.addVertex(targetV);
            fullGraph.addVertex(memAccessV);
            fullGraph.addVertex(sumV);
            fullGraph.addEdge(rAV, sumV);
            fullGraph.addEdge(sumV, memAccessV);

            //Build rB graph
            if (op2.isImmediate()) {
                String immVal = op2.getRepresentation();
                var immV = new AddressVertex(immVal, AddressVertexType.IMMEDIATE);
                
                //TODO: assign BASE_ADDR and OFFSET when an IMM is present
                // Below assumption is not always true
                // Use later transform for a more educated guess
                rAV.setProperty(AddressVertexProperty.BASE_ADDR);
                immV.setProperty(AddressVertexProperty.OFFSET);

                
                fullGraph.addVertex(immV);
                fullGraph.addEdge(immV, sumV);
            } else {
                var rBBuilder = new PartialAddressGraphBuilder(op2Reg, startIdx - 1, basicBlock);
                var rBGraph = rBBuilder.generateGraph();
                Graphs.addGraph(fullGraph, rBGraph);
                var rBV = rBBuilder.getRoot();
                rBV.setIsaInfo(AddressVertexIsaInfo.RB);
                
                //Set rA and rB using MicroBlaze GCC conventions
                rAV.setProperty(AddressVertexProperty.OFFSET);
                rBV.setProperty(AddressVertexProperty.BASE_ADDR);
                fullGraph.addEdge(rBV, sumV);
            }
            
            // Connect load/store destination/source register to the rest
            if (inst.isLoad())
                fullGraph.addEdge(memAccessV, targetV);
            else
                fullGraph.addEdge(targetV, memAccessV);
            
            return fullGraph;
        }
        return null;
    }

    @Deprecated
    private List<String> findContributingRegisters(Operand op) {
        var regs = new ArrayList<String>();
        regs.add(AnalysisUtils.getRegName(op));

        for (int i = startIdx - 1; i >= 0; i--) {
            Instruction inst = this.basicBlock.get(i);
            var ops = inst.getData().getOperands();

            if (ops.size() != 0) {
                var op0 = ops.get(0);
                var op0name = AnalysisUtils.getRegName(op0);

                if (op0.isWrite() && regs.contains(op0name)) {
                    for (int j = 1; j < ops.size(); j++) {
                        if (ops.get(j).isRegister())
                            regs.add(AnalysisUtils.getRegName(ops.get(j)));
                    }
                }
            }
        }
        return regs.stream().distinct().collect(Collectors.toList());
    }
}

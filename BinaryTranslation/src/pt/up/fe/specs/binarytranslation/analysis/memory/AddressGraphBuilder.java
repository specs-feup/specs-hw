package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
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
            Operand base = inst.getData().getOperands().get(1);
            Operand offset = inst.getData().getOperands().get(2);    

            String targetReg = AnalysisUtils.getRegName(target);
            String baseReg = AnalysisUtils.getRegName(base);
            String offsetReg = AnalysisUtils.getRegName(offset);

            //Build memory access node
            var targetV = new AddressVertex(targetReg, AddressVertexType.REGISTER);
            var memAccessV = new AddressVertex(inst.isLoad() ? "Memory Access (load)" : "Memory Access (store)",
                    AddressVertexType.MEMORY);
            var sumV = new AddressVertex("+", AddressVertexType.OPERATION);

            //Build base address graph
            var baseBuilder = new PartialAddressGraphBuilder(baseReg, startIdx - 1, basicBlock);
            var fullGraph = baseBuilder.generateGraph();
            var baseV = baseBuilder.getRoot();
            baseV.setProperty(AddressVertexProperty.BASE_ADDR_START);

            //Add memory access to base addr graph, and connect
            fullGraph.addVertex(targetV);
            fullGraph.addVertex(memAccessV);
            fullGraph.addVertex(sumV);
            fullGraph.addEdge(baseV, sumV);
            fullGraph.addEdge(sumV, memAccessV);

            //Build offset graph
            if (offset.isImmediate()) {
                String immVal = offset.getRepresentation();
                var immV = new AddressVertex(immVal, AddressVertexType.IMMEDIATE);
                immV.setProperty(AddressVertexProperty.OFFSET_START);
                fullGraph.addVertex(immV);
                fullGraph.addEdge(immV, sumV);
            } else {
                var offsetBuilder = new PartialAddressGraphBuilder(offsetReg, startIdx - 1, basicBlock);
                var offsetGraph = offsetBuilder.generateGraph();
                Graphs.addGraph(fullGraph, offsetGraph);
                var immV = offsetBuilder.getRoot();
                immV.setProperty(AddressVertexProperty.OFFSET_START);
                fullGraph.addEdge(immV, sumV);
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

package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;

public class InductionVariablesDetector extends APropertyDetector {
    public InductionVariablesDetector(BinarySegment bb, List<Instruction> insts) {
        super(bb, insts);
    }

    public HashMap<String, Long> registerDiff(RegisterDump r1, RegisterDump r2) {
        var diff = new HashMap<String, Long>();
        for (var k : r1.getRegisterMap().keySet()) {
            long i1 = r1.getRegisterMap().get(k);
            long i2 = r2.getRegisterMap().get(k);
            long id = i2 - i1;
            diff.put(k, id);
        }
        return diff;
    }

    public void printDifferences() {
        var keys = tracker.getOccurrences().get(0).getRegisters().getRegisterMap().keySet();
        var diffs = new ArrayList<HashMap<String, Long>>();
        for (int i = 0; i < tracker.getOccurrences().size() - 1; i++) {
            var b1 = tracker.getOccurrences().get(i).getRegisters();
            var b2 = tracker.getOccurrences().get(i + 1).getRegisters();
            var bd = registerDiff(b1, b2);
            diffs.add(bd);
        }

        var sb = new StringBuilder();
        for (var k : keys) {
            boolean isZero = true;
            for (var diff : diffs) {
                if (diff.get(k) != 0)
                    isZero = false;
            }
            if (!isZero) {
                sb.append(k).append(": ");
                for (var diff : diffs) {
                    sb.append(diff.get(k)).append("| ");
                }
                sb.append("\n");
            }

        }
        System.out.println(sb.toString());
    }

    public void printInstDifferences() {
        var rows = tracker.getBasicBlock().getInstructions().size();
        var cols = tracker.getOccurrences().size();
        String[][] regs = new String[rows][cols];
        for (int col = 0; col < tracker.getOccurrences().size(); col++) {
            var bb = tracker.getOccurrences().get(col);
            for (int row = 0; row < bb.getInsts().size(); row++) {
                var inst = bb.getInsts().get(row);
                String regInfo = getRelevantRegs(inst);
                regs[row][col] = regInfo;
            }
        }

        var sb = new StringBuilder();
        for (int i = 0; i < regs.length; i++) {
            for (int j = 0; j < regs[i].length; j++) {
                sb.append(regs[i][j]).append("| ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    private String getRelevantRegs(Instruction inst) {
        StringBuilder sb = new StringBuilder();
        for (var op : inst.getData().getOperands()) {
            if (op.isRegister() && op.isRead()) {
                String regName = AnalysisUtils.getRegName(op);
                long val = inst.getRegisters().getValue(regName);
                sb.append(regName).append("{").append(val).append("} ");
            }
        }
        return sb.toString();
    }

    public ArrayList<String> detectVariables(Graph<AddressVertex, DefaultEdge> mergedGraph) {
        var res = new ArrayList<String>();
        var regs = tracker.getRegisters();
        String[][] fullRes = new String[regs.size()][4];

        for (int i = 0; i < regs.size(); i++) {
            String reg = regs.get(i);

            boolean c1 = conditionPartOfAddress(reg, mergedGraph);
            boolean c2 = conditionHasIncrement(reg);
            boolean c3 = conditionIsInComparison(reg);
            if (c1 && c2 && c3)
                res.add(reg);

            // For full output
            fullRes[i][0] = reg;
            fullRes[i][1] = Boolean.toString(c1);
            fullRes[i][2] = Boolean.toString(c2);
            fullRes[i][3] = Boolean.toString(c3);
        }
        printFullResult(fullRes);
        return res;
    }

    private void printFullResult(String[][] fullRes) {
        for (int i = 0; i < fullRes.length; i++) {
            var sb = new StringBuilder(fullRes[i][0] + ": ");
            sb.append(fullRes[i][1] + ", ");
            sb.append(fullRes[i][2] + ", ");
            sb.append(fullRes[i][3]);
            System.out.println(sb.toString());
        }
    }

    private boolean conditionPartOfAddress(String reg, Graph<AddressVertex, DefaultEdge> mergedGraph) {
        for (var vertex : mergedGraph.vertexSet()) {
            if (vertex.getType() == AddressVertexType.REGISTER) {
                if (vertex.getLabel().equals(reg))
                    return true;
            }
        }
        return false;
    }

    private boolean conditionHasIncrement(String reg) {
        for (var inst : tracker.getBasicBlockInsts()) {
            if (inst.isAdd() || inst.isSub() || inst.isMul()) {
                if (inst.getData().getOperands().size() == 3) {

                    var op0 = inst.getData().getOperands().get(0);
                    var op1 = inst.getData().getOperands().get(1);
                    var op3 = inst.getData().getOperands().get(2);

                    if (op0.isRegister() && op1.isRegister() && op3.isImmediate()) {
                        var reg0 = AnalysisUtils.getRegName(op0);
                        var reg1 = AnalysisUtils.getRegName(op1);
                        if (reg0.equals(reg) && reg1.equals(reg))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean conditionIsInComparison(String reg) {
        for (var inst : tracker.getBasicBlockInsts()) {
            if (inst.isLogical()) {
                for (var op : inst.getData().getOperands()) {
                    if (op.isRegister()) {
                        var currReg = AnalysisUtils.getRegName(op);
                        if (currReg.equals(reg))
                            return true;
                    }
                }
            }
        }
        return false;
    }
}

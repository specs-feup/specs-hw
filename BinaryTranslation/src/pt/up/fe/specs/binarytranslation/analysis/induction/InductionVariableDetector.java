package pt.up.fe.specs.binarytranslation.analysis.induction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;

public class InductionVariableDetector {
    private BasicBlockOccurrenceTracker tracker;

    public InductionVariableDetector(BinarySegment bb, List<Instruction> insts) {
        this.tracker = new BasicBlockOccurrenceTracker(bb, insts);
    }
    
    public void printOccurrenceRegisters() {
        for (var o : tracker.getOccurrences()) {
            var regs = o.getRegisters();
            regs.prettyPrint();
            AnalysisUtils.printSeparator(40);
        }
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
            sb.append(k).append(": ");
            for (var diff : diffs) {
                sb.append(diff.get(k)).append("| ");
            }
            sb.append("\n");
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
}

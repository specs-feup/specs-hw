package pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.ocurrence.BasicBlockOccurrence;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.ocurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class OutEliminationOccurrence {

    private List<Instruction> insts;
    private BasicBlockOccurrence bb;
    private ArrayList<String> outs;
    private BasicBlockOccurrenceTracker tracker;

    public OutEliminationOccurrence(BasicBlockOccurrence bb, 
                                    BasicBlockOccurrenceTracker tracker, 
                                    List<Instruction> insts, 
                                    InstructionSets inouts) {
        this.bb = bb;
        this.tracker = tracker;
        this.insts = insts;
        this.outs = inouts.setToList(inouts.getOutSet());
    }

    public ArrayList<String> eliminate(int window) {
        System.out.println(bb.toString());
        System.out.println("Using a window of " + window + " instructions");
        
        var toEliminate = new ArrayList<String>();
        
        int curr = bb.getEndPos() + 1;
        for (int i = curr; i < curr + window; i++) {
            if (i >= insts.size()) {
                System.out.println("Trace finished before window could reach the end");
                return toEliminate;
            }
                
            var next = insts.get(i);
            //System.out.println("Processing inst " + i);
            if (tracker.basicBlockFromPosition(i) != null) {
                System.out.println("Cannot proceed any further, another BB occurrence was reached");
                AnalysisUtils.printSeparator(40);
                return toEliminate;
            }
            
            for (var op : next.getData().getOperands()) {
                if (op.isRegister()) {
                    String regName = AnalysisUtils.getRegName(op);
                    if (outs.contains(regName)) {
                        if (op.isRead()) {
                            //System.out.println("Out register " + regName + " is used");
                            outs.remove(regName);
                        }
                        if (op.isWrite() && outs.contains(regName)) {
                            System.out.println("Out register " + regName + " is defined before it is used");
                            toEliminate.add(regName);
                            outs.remove(regName);
                        }
                    }
                }
            }       
        }
        AnalysisUtils.printSeparator(40);
        return toEliminate;
    }
}

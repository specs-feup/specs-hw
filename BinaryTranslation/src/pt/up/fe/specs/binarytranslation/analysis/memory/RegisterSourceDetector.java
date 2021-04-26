package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.Stack;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class RegisterSourceDetector extends APropertyDetector {

    public RegisterSourceDetector(BasicBlockOccurrenceTracker tracker) {
        super(tracker);
    }

    public void findSource(String register) {
        int i = getTracker().getOccurrences().get(0).getStartPos();
        var stack = new Stack<Instruction>();

        while (i >= 0) {
            var inst = getTracker().getTrace().get(i);
            for (var op : inst.getData().getOperands()) {
                if (op.isRegister()) {
                    if (AnalysisUtils.getRegName(op).equals(register))
                        stack.add(inst);
                }
            }
            i--;
        }
        //AnalysisUtils.printInstructionList(new ArrayList<Instruction>(stack));
    }
}

package pt.up.fe.specs.binarytranslation.analysis.prologue;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.APropertyDetector;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.asm.RegisterProperties;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class PrologueDetector extends APropertyDetector {

    private RegisterProperties props;

    public PrologueDetector(BasicBlockOccurrenceTracker tracker, RegisterProperties props) {
        super(tracker);
        this.props = props;
    }

    public ArrayList<Instruction> findPrologue(String register) {
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
        return new ArrayList<Instruction>(stack);
    }
    
    public Map<String, String> getRegisterInits() {
        var regState = props.getAllRegisters();
        return null;
    }
}

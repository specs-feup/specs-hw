package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.asm.RegisterProperties;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class PrologueDetector extends APropertyDetector {
    private static int depth = 10;
    private RegisterProperties props;

    public PrologueDetector(BasicBlockOccurrenceTracker tracker, RegisterProperties props) {
        super(tracker);
        this.props = props;
    }

    private List<Instruction> findPrologue() {
        int i = getTracker().getOccurrences().get(0).getStartPos();
        var stack = new Stack<Instruction>();
        int stop = i - depth;

        while (i >= stop) {
            var inst = getTracker().getTrace().get(i);
            stack.add(inst);
            i--;
        }
        return new ArrayList<Instruction>(stack);
    }

    private Map<String, List<String>> getRegisterInitState() {
        var regs = props.getAllRegisters();
        var regState = new HashMap<String, List<String>>();
        for (var reg : regs) {
            regState.put(reg, new ArrayList<String>());
        }
        return regState;
    }

    public Map<String, List<String>> getRegisterState() {
        var prologue = findPrologue();
        var state = getRegisterInitState();

        for (var inst : prologue) {
            if (inst.isLogical() || inst.isAdd() || inst.isSub() || inst.isMul() /*|| inst.isUnary()*/) {
                var opRD = inst.getData().getOperands().get(0);
                String rD = AnalysisUtils.getRegName(opRD);
                var replace = getReplacement(rD, state, inst);
                state.put(rD, replace);
            }
        }
        return cleanState(state);
    }

    private Map<String, List<String>> cleanState(Map<String, List<String>> state) {
        for (var key : state.keySet()) {
            var regList = state.get(key);
            var cleansed = regList.stream().distinct().collect(Collectors.toList());
            for (int i = 0; i < cleansed.size(); i++) {
                var elem = cleansed.get(i);
                if (elem.contains("0x"))
                    cleansed.set(i, AnalysisUtils.hexToDec(elem));
                if (props.isZero(elem))
                    cleansed.set(i, "0");
            }
            state.put(key, cleansed);
        }
        return state;
    }

    private List<String> getReplacement(String rD, Map<String, List<String>> state, Instruction inst) {
        var preexisting = state.get(rD);
        for (int i = 1; i < inst.getData().getOperands().size(); i++) {
            var op = inst.getData().getOperands().get(i);
            if (op.isRegister()) {

                var opReg = AnalysisUtils.getRegName(op);
                var opPreexisting = state.get(opReg);

                if (opPreexisting.size() == 0) {
                    preexisting.add(opReg);
                } else {
                    preexisting.addAll(opPreexisting);
                }
            }
            if (op.isImmediate()) {
                preexisting.add(op.getRepresentation());
            }
        }
        return preexisting;
    }

    public static void printPrologueState(Map<String, List<String>> state) {
        System.out.println("Prologue state with window = " + depth);
        for (var key : state.keySet()) {
            var list = state.get(key);
            if (list.size() != 0) {
                System.out.println(key + " <- " + list.toString());
            }
        }
        System.out.println("(Only registers with dependencies are shown)");
    }
}

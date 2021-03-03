package pt.up.fe.specs.binarytranslation.analysis.basicblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class BasicBlockInOuts {
    private List<Instruction> insts;
    private ArrayList<String> regs;

    public BasicBlockInOuts(List<Instruction> insts) {
        this.insts = insts;
        this.regs = findAllRegistersOfSeq(insts);
    }

    public void calculateInOuts() {
        System.out.println("use/def registers: " + regs.toString());
        HashMap<Instruction, InstructionSets> instSets = new HashMap<>();
        
        for (Instruction i : insts) {
            InstructionSets sets = new InstructionSets(i, regs);
            instSets.put(i, sets);
            //fill def and use
        }
    }

    public static ArrayList<String> findAllRegistersOfSeq(List<Instruction> insts) {
        ArrayList<String> lst = new ArrayList<>();
        
        for (Instruction i : insts) {
            for (Operand op : i.getData().getOperands()) {
                if (op.isRegister()) {
                    String reg = op.getProperties().getPrefix() + op.getStringValue();
                    lst.add(reg);
                }
            }
        }
        var newList = lst.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(newList);
        return newList;
    }
}

package pt.up.fe.specs.binarytranslation.analysis.inouts;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public abstract class ASequenceInOuts {
    protected List<Instruction> insts;
    protected ArrayList<String> regs;
    protected ArrayList<InstructionSets> sets;

    public ASequenceInOuts(List<Instruction> insts) {
        this.insts = insts;
        this.regs = findAllRegistersOfSeq(insts);
        this.sets = new ArrayList<>();
    }

    public void calculateInOuts() {
        for (Instruction i : insts) {
            InstructionSets is = new InstructionSets(regs);
            findUseDefs(i, is);
            sets.add(is);
        }
        String oldHash = "";
        boolean changing = true;
        int iter = 0;

        while (changing) {
            String newHash = doIteration(sets);
            if (newHash.compareTo(oldHash) == 0)
                changing = false;
            else
                oldHash = newHash;
            iter++;
        }
    }

    public abstract void printResult();

    protected String doIteration(ArrayList<InstructionSets> sets) {
        String hash = "";
        for (int i = sets.size() - 1; i >= 0; i--) {
            InstructionSets currSets = sets.get(i);
            if (i < sets.size() - 1) {
                InstructionSets succSets = sets.get(i + 1);

                // out[n] = union of in[s], where s = successor
                currSets.setOutSet(succSets.getInSet());

                // in[n] = use[n] union (out[n] - def[n])
                BitSet def = currSets.getDefSet();
                BitSet use = currSets.getUseSet();

                BitSet diff = (BitSet) currSets.getOutSet().clone();
                diff.andNot(def);
                diff.or(use);
                currSets.setInSet(diff);
            }
            hash += currSets.getHash();
        }
        return hash;
    }

    public void printSequenceInOuts() {
        for (int i = 0; i < insts.size(); i++) {
            printInstWithSets(i);
        }
    }

    protected void printInstWithSets(int i) {
        InstructionSets is = sets.get(i);
        String str = String.format("%-40s", insts.get(i).getString()) + is.toString();
        System.out.println(str);
    }
    
    public void printUseDefRegisters() {
        System.out.println("\nuse/def registers: " + regs.toString());
    }
    
    public static void findUseDefs(Instruction i, InstructionSets sets) {
        for (Operand op : i.getData().getOperands()) {
            if (op.isRegister()) {
                String reg = AnalysisUtils.getRegName(op);
                if (op.isRead())
                    sets.setUse(reg);
                if (op.isWrite())
                    sets.setDef(reg);
            }
        }
    }

    public static ArrayList<String> findAllRegistersOfSeq(List<Instruction> insts) {
        ArrayList<String> lst = new ArrayList<>();

        for (Instruction i : insts) {
            for (Operand op : i.getData().getOperands()) {
                if (op.isRegister()) {
                    String reg = AnalysisUtils.getRegName(op);
                    lst.add(reg);
                }
            }
        }
        Comparator<String> regCompare = (String r1, String r2) -> {
            r1 = r1.replaceAll("[^\\d.]", "");
            r2 = r2.replaceAll("[^\\d.]", "");
            int n1 = Integer.parseInt(r1);
            int n2 = Integer.parseInt(r2);
            return n1 > n2 ? 1 : -1;
        };
        // Remove duplicates
        var newList = lst.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
        // Sort array with custom comparator
        Collections.sort(newList, regCompare);
        return newList;
    }
}

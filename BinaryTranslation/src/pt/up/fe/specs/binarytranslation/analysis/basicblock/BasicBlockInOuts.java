package pt.up.fe.specs.binarytranslation.analysis.basicblock;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.TraceBasicBlock;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class BasicBlockInOuts {
    private TraceBasicBlock bb;
    private List<Instruction> insts;
    private ArrayList<String> regs;
    private ArrayList<InstructionType> loadstores = new ArrayList<>();
    {
        loadstores.add(InstructionType.G_LOAD);
        loadstores.add(InstructionType.G_STORE);
    }

    public BasicBlockInOuts(BinarySegment seg) {
        seg.printSegment();
        this.bb = (TraceBasicBlock) seg;
        this.insts = seg.getInstructions();
        this.regs = findAllRegistersOfSeq(insts);
    }

    public void calculateInOuts() {
        ArrayList<InstructionSets> sets = new ArrayList<>();

        for (Instruction i : insts) {
            InstructionSets is = new InstructionSets(i, regs);
            findUseDefs(i, is);
            sets.add(is);
        }
        String oldHash = "";
        boolean changing = true;
        int iter = 0;

        while (changing) {
            String newHash = doIteration(sets);
            System.out.println("Iter " + iter + ": " + newHash);
            if (newHash.compareTo(oldHash) == 0)
                changing = false;
            else
                oldHash = newHash;
            iter++;
            // FOR DEBUG ONLY!
            // changing = iter != 10;
        }

        printUseDefs(sets);

        printResult(sets);
    }

    private void printResult(ArrayList<InstructionSets> sets) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Basic Block In/Outs:\n");
        sb.append("In: ");
        for (int i = 0; i < sets.get(0).getInSet().length(); i++) {
            if (sets.get(0).getInSet().get(i))
                sb.append(regs.get(i) + " ");
        }
        
        sb.append("\nOut: ");
        BitSet outs = calculateBBOuts(sets);
        for (int i = 0; i < outs.length(); i++) {
            if (outs.get(i))
                sb.append(regs.get(i) + " ");
        }
        
        sb.append("\n");
        System.out.println(sb.toString());
    }

    private String doIteration(ArrayList<InstructionSets> sets) {
        String hash = "";
        //for (int i = sets.size() - 1; i >= 0; i--) {
        for (int i = 0; i < sets.size(); i++) {
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

    private void printUseDefs(ArrayList<InstructionSets> sets) {
        System.out.println("\nuse/def registers: " + regs.toString());
        for (int i = 0; i < insts.size(); i++) {
            InstructionSets is = sets.get(i);
            String str = String.format("%-40s", insts.get(i).getString()) + is.toString();
            System.out.println(str);
        }

    }

    private void findUseDefs(Instruction i, InstructionSets sets) {
        for (Operand op : i.getData().getOperands()) {
            if (op.isRegister()) {
                String reg = getRegName(op);
                if (op.isRead())
                    sets.setUse(reg);
                if (op.isWrite())
                    sets.setDef(reg);
            }
        }
    }

    public static String getRegName(Operand op) {
        return op.getProperties().getPrefix() + op.getStringValue();
    }

    public static ArrayList<String> findAllRegistersOfSeq(List<Instruction> insts) {
        ArrayList<String> lst = new ArrayList<>();

        for (Instruction i : insts) {
            for (Operand op : i.getData().getOperands()) {
                if (op.isRegister()) {
                    String reg = getRegName(op);
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
    
    public BitSet calculateBBOuts(ArrayList<InstructionSets> sets) {
        BitSet outs = new BitSet(regs.size());
        for (InstructionSets s : sets) {
            BitSet out = s.getDefSet();
            outs.or(out);
        }
        return outs;
    }
}

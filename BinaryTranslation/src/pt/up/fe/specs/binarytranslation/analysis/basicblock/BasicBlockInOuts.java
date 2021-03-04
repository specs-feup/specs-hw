package pt.up.fe.specs.binarytranslation.analysis.basicblock;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
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
        String hash = doIteration(sets);
        System.out.println(hash);
        
        printUseDefs(sets);
    }
    
    private String doIteration(ArrayList<InstructionSets> sets) {
        String hash = "";
        for (int i = sets.size() - 1; i >= 0; i--) {
            InstructionSets currSets = sets.get(i);
            if (i < sets.size() - 1) {
                InstructionSets succSets = sets.get(i + 1);
                
                //out[n] = union of in[s], where s = successor
                currSets.setOutSet(succSets.getInSet());
                
                //in[n] = use[n] union (out[n] - def[n])
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
        // For load/stores
        if (!Collections.disjoint(i.getData().getGenericTypes(), loadstores)) {
            Operand op0 = i.getData().getOperands().get(0);
            String reg0 = getRegName(op0);
            sets.setDef(reg0);
            Operand op1 = i.getData().getOperands().get(1);
            String reg1 = getRegName(op1);
            sets.setUse(reg1);
            Operand op2 = i.getData().getOperands().get(2);
            if (op2.isRegister()) {
                String reg2 = getRegName(op2);
                sets.setUse(reg2);
            }
        }
        // For other insts
        else {
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
            r1 = r1.replace("r", "");
            r2 = r2.replace("r", "");
            int n1 = Integer.parseInt(r1);
            int n2 = Integer.parseInt(r2);
            return n1 > n2 ? 1 : -1;
        };
        //Remove duplicates
        var newList = lst.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
        //Sort array with custom comparator
        Collections.sort(newList, regCompare);
        return newList;
    }
}

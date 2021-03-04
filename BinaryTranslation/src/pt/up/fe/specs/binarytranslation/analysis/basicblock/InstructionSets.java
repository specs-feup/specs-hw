package pt.up.fe.specs.binarytranslation.analysis.basicblock;

import java.util.ArrayList;
import java.util.BitSet;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class InstructionSets {
    private Instruction inst;
    private ArrayList<String> regs;
    private BitSet use;
    private BitSet def;
    private BitSet in;
    private BitSet out;

    public InstructionSets(Instruction inst, ArrayList<String> regs) {
        this.inst = inst;
        this.regs = regs;
        this.use = new BitSet(regs.size());
        this.def = new BitSet(regs.size());
        this.in = new BitSet(regs.size());
        this.out = new BitSet(regs.size());
    }
    
    public void setUse(String reg) {
        setRegister(reg, use, true);
    }
    
    public void setDef(String reg) {
        setRegister(reg, def, true);
    }
    
    public void setIn(String reg) {
        setRegister(reg, in, true);
    }
    
    public void setOut(String reg) {
        setRegister(reg, out, true);
    }
    
    private void setRegister(String reg, BitSet bitset, boolean set) {
        int bit = regs.indexOf(reg);
        if (set)
            bitset.set(bit);
        else
            bitset.clear(bit);
    }

    public Instruction getInst() {
        return inst;
    }

    public BitSet getUse() {
        return use;
    }

    public BitSet getDef() {
        return def;
    }

    public BitSet getIn() {
        return in;
    }

    public BitSet getOut() {
        return out;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" use").append(bitsetString(use));
        sb.append(" def").append(bitsetString(def));
        sb.append(" in").append(bitsetString(in));
        sb.append(" out").append(bitsetString(out));
        return sb.toString();
    }
    
    private String bitsetString(BitSet bs) {
        var regsToPrint = new ArrayList<String>();
        for (int i = 0; i < bs.length(); i++) {
            if (bs.get(i)) {
                regsToPrint.add(regs.get(i));
            }
        }
        return "{" + String.join(",", regsToPrint) + "}";
    }
}

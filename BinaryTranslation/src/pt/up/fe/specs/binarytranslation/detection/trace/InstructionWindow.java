package pt.up.fe.specs.binarytranslation.detection.trace;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class InstructionWindow {

    private int windowSize;
    private List<Instruction> ilist;

    public InstructionWindow(int windowSize) {
        this.windowSize = windowSize;
        this.ilist = new ArrayList<Instruction>(this.windowSize);
    }

    public Instruction add(Instruction inst) {

        // slide
        if (this.ilist.size() == this.windowSize)
            this.ilist.remove(0);

        this.ilist.add(inst);
        return inst;
    }

    public boolean isFull() {
        return this.ilist.size() == this.windowSize;
    }

    public boolean exists(Integer addr) {
        for (var inst : this.ilist)
            if (inst.getAddress().intValue() == addr.intValue())
                return true;
        return false;
    }

    public boolean exists(Instruction inst) {
        return this.ilist.contains(inst);
    }

    public List<Instruction> getWindow() {
        return ilist;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public Instruction get(int index) {
        return this.ilist.get(index);
    }

    /*
     * Get latest occuring instance of an instruction (by addr) in window
     */
    public Instruction getLatestByAddr(Integer addr) {
        for (int i = this.ilist.size() - 1; i >= 0; i--)
            if (this.ilist.get(i).getAddress().intValue() == addr.intValue())
                return this.ilist.get(i);
        return null;
    }

    /*
     * Get latest occuring instance of an instruction (by addr) in window
     */
    public Instruction getEarliestByAddr(Integer addr) {
        for (var inst : this.ilist)
            if (inst.getAddress().intValue() == addr.intValue())
                return inst;
        return null;
    }

    public Instruction getFromLast(int index) {
        return this.ilist.get(this.ilist.size() - 1 - index);
    }

    public Instruction getLast() {
        return this.ilist.get(this.ilist.size() - 1);
    }

    public List<Instruction> getRange(Instruction from, Instruction to) {
        var sidx = this.ilist.indexOf(from);
        var eidx = this.ilist.indexOf(to);
        return this.ilist.subList(sidx, eidx + 1);
    }
}

package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.Operand;

public class ABinarySegment implements BinarySegment {

    protected SegmentType segtype;
    protected List<Instruction> instlist;
    protected List<Operand> liveins = null, liveouts = null;

    public ABinarySegment() {
        this.instlist = new ArrayList<Instruction>();
    }

    public int getSegmentLength() {
        return instlist.size();
    }

    public SegmentType getSegmentType() {
        return this.segtype;
    }

    public List<Operand> getLiveIns() {
        return this.liveins;
    }

    public List<Operand> getLiveOuts() {
        return this.liveouts;
    }

    public int getTotalLatency() {
        int totlat = 0;
        for (int i = 0; i < instlist.size(); i++)
            totlat += instlist.get(i).getLatency();
        return totlat;
    }

    public List<Instruction> getInstructions() {
        return this.instlist;
    }

    public void printSegment() {
        for (Instruction inst : this.instlist) {
            inst.printInstruction();
        }
    }

    /*
     * Call to build liveins and liveouts list, only after segment is complete
     */
    protected void buildLiveInsAndLiveOuts() {

        this.liveins = new ArrayList<Operand>();
        this.liveouts = new ArrayList<Operand>();

        for (Instruction i : this.instlist) {
            for (Operand op : i.getData().getOperands()) {

                // everything that is a register is a livein
                if (op.isRegister() && op.isRead() && !this.liveins.contains(op)) {
                    this.liveins.add(op);
                }

                // only liveout if written
                if (op.isRegister() && op.isWrite() && !this.liveouts.contains(op)) {
                    this.liveouts.add(op);
                }
            }
        }

        // remove duplicates (a duplicate has same type (i.e., read or write) and value)
        Iterator<Operand> it = this.liveins.iterator();
        while (it.hasNext()) {
            for (Operand op : this.liveins) {
                var next = it.next();
                if (next != op && next.isEqual(op))
                    it.remove();
            }
        }

        // remove duplicates (a duplicate has same type (i.e., read or write) and value)
        it = this.liveouts.iterator();
        while (it.hasNext()) {
            for (Operand op : this.liveouts) {
                var next = it.next();
                if (next != op && next.isEqual(op))
                    it.remove();
            }
        }

        return;
    }
}

package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.Instruction;

public class BasicBlock implements BinarySegment {

    private SegmentType segtype = SegmentType.BASIC_BLOCK;
    private List<Instruction> instlist;
    private boolean terminated = false;
    // value must be true if an instruction is added
    // which is a backwards branch; no other instructions
    // may be added

    /*
     * Constructor for building the BB inst by inst later
     */
    public BasicBlock() {
        this.instlist = new ArrayList<Instruction>();
    }

    /*
     * Constructor builds the BB on the spot with an existing list
     * Does NOT ensure the BB is terminated, only if the last inst in
     * the ilist is a backwards branch
     */
    public BasicBlock(List<Instruction> ilist) {
        this.instlist = new ArrayList<Instruction>();

        // build the entire block
        for (Instruction i : ilist) {
            this.addInst(i);
        }
    }

    public void addInst(Instruction newinst) {
        if (this.terminated == true) {
            // TODO proper exception handling
            return;
        } else {
            this.instlist.add(newinst);

            // if added inst is branch, prevent addition of new insts
            if (newinst.isBackwardsJump() && newinst.isConditionalJump())
                this.terminated = true;
        }
    }

    @Override
    public List<Integer> getLiveIns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Integer> getLiveOuts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getTotalLatency() {
        int totlat = 0;
        for (int i = 0; i < instlist.size(); i++)
            totlat += instlist.get(i).getLatency();
        return totlat;
    }

    @Override
    public int getSegmentLength() {
        return instlist.size();
    }

    @Override
    public SegmentType getSegmentType() {
        return this.segtype;
    }
}

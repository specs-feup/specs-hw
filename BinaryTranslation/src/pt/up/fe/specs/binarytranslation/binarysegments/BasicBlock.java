package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.Instruction;

public class BasicBlock implements BinarySegment {

    private SegmentType segtype = SegmentType.BASIC_BLOCK;
    private List<Instruction> instlist;
    private boolean terminated = false;
    private int delaycounter = 0;
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

        // cannot (should not!) add any more
        if (this.terminated == true && this.delaycounter == 0) {
            // TODO proper exception handling
            return;
        }

        // should not be able to add more branches
        else if (this.terminated == true && newinst.isJump()) {
            // TODO proper exception handling
            return;
        }

        // should not be able to add forward jumps
        else if (newinst.isJump() && !newinst.isBackwardsJump()) {
            // TODO proper exception handling
            return;
        }

        /// add
        else {

            this.instlist.add(newinst);

            // countdown to pipe fill due to delay
            if (this.delaycounter > 0) {
                this.delaycounter--;
            }

            // if added inst is branch allow
            // addition of at most getDelay insts after this
            if (newinst.isBackwardsJump() && newinst.isConditionalJump()) {
                this.terminated = true; // only one branch permitted
                this.delaycounter = newinst.getDelay();
            }
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

    @Override
    public List<Instruction> getInstructions() {
        return this.instlist;
    }
}

package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.Instruction;

public class FrequentSequence implements BinarySegment {

    private SegmentType segtype = SegmentType.STATIC_FREQUENT;
    private List<Instruction> instlist;

    /*
     * Constructor for building the BB inst by inst later
     */
    public FrequentSequence() {
        this.instlist = new ArrayList<Instruction>();
    }

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    public FrequentSequence(List<Instruction> ilist) {
        this.instlist = new ArrayList<Instruction>();

        // build the entire block
        for (Instruction i : ilist) {
            this.addInst(i);
        }
    }

    public void addInst(Instruction newinst) {

        if (newinst.isJump()) {
            // TODO proper exception handling
            return;
        }

        this.instlist.add(newinst);
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

    @Override
    public void printSegment() {
        for (Instruction inst : this.instlist) {
            inst.printInstruction();
        }
    }
}

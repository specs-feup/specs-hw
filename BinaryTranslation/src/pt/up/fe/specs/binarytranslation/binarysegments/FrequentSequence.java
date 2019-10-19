package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.List;

import pt.up.fe.specs.binarytranslation.Instruction;

public class FrequentSequence implements BinarySegment {

    private SegmentType segtype = SegmentType.STATIC_FREQUENT;
    private List<Instruction> instlist; // TODO should be instrucitonproperties??
    private List<Integer> startAddresses;
    private int sequencehash;

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    private FrequentSequence(List<Instruction> ilist) {
        this.instlist = ilist;

        // regenerate hashstring
        String hashstring = "";
        for (Instruction i : ilist) {
            hashstring += i.getName();
        }
        this.sequencehash = hashstring.hashCode();
    }

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    public FrequentSequence(List<Instruction> ilist, List<Integer> startAddresses) {
        this(ilist);
        this.startAddresses = startAddresses;
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
        System.out.print("Sequence=[hashcode: " + this.sequencehash + "; " + this.startAddresses + "]\n");
        for (Instruction inst : this.instlist) {
            inst.printInstruction();
        }
    }
}

package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.Instruction;

public class FrequentSequence implements BinarySegment {

    private SegmentType segtype = SegmentType.STATIC_FREQUENT;
    private List<Instruction> instlist;
    private List<Integer> startAddresses;

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    private FrequentSequence(List<Instruction> ilist) {
        this.instlist = ilist;
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
        List<String> hexes = new ArrayList<String>();
        for (Integer addr : this.startAddresses)
            hexes.add("0x" + Integer.toHexString(addr));

        System.out.print("Sequence occurs at=" + hexes + "\n");
        for (Instruction inst : this.instlist) {
            inst.printInstruction();
        }
    }
}

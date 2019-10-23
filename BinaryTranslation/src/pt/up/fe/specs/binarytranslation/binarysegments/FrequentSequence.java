package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class FrequentSequence extends ABinarySegment {

    private SegmentType segtype = SegmentType.STATIC_FREQUENT;
    private List<Integer> startAddresses;

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    private FrequentSequence(List<Instruction> ilist) {
        super();
        this.instlist = ilist;
        buildLiveInsAndLiveOuts();
    }

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    public FrequentSequence(List<Instruction> ilist, List<Integer> startAddresses) {
        this(ilist);
        this.startAddresses = startAddresses;
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

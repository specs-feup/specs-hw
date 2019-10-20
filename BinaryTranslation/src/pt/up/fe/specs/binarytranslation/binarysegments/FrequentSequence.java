package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.InstructionProperties;

public class FrequentSequence implements BinarySegment {

    private SegmentType segtype = SegmentType.STATIC_FREQUENT;
    private List<InstructionProperties> instlist;
    private List<Integer> startAddresses;
    private int sequencehash;

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    private FrequentSequence(List<InstructionProperties> ilist) {
        this.instlist = ilist;

        // regenerate hashstring
        String hashstring = "";
        for (InstructionProperties i : ilist) {
            hashstring += Integer.toHexString(i.getOpCode());    
        }
        this.sequencehash = hashstring.hashCode();
    }

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    public FrequentSequence(List<InstructionProperties> ilist, List<Integer> startAddresses) {
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
    public List<InstructionProperties> getProperties() {
        return this.instlist;
    }
    
    @Override
    public List<Instruction> getInstructions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void printSegment() {
        List<String> hexes = new ArrayList<String>();
        for(Integer addr : this.startAddresses)
            hexes.add("0x" + Integer.toHexString(addr));
         
        System.out.print("Sequence=[hashcode: " + this.sequencehash + "; " + hexes + "]\n");
        for (InstructionProperties inst : this.instlist) {
            System.out.print(inst.getEnumName() + "\n");
        }
        System.out.print("\n");
    }
}

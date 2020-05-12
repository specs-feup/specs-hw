package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.ApplicationInformation;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public abstract class AFrequentSequence extends ABinarySegment {

    /**
     * 
     */
    private static final long serialVersionUID = 3528223632861256L;

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    protected AFrequentSequence(List<Instruction> ilist,
            List<SegmentContext> contexts, ApplicationInformation appinfo) {
        super(ilist, contexts, appinfo);
    }

    /*
     * Must be overriden by children
     */
    abstract protected List<Integer> getAddresses();

    /*
     * Must be overriden by children
     */
    abstract protected String getAddressListRepresentation();

    /*
     * 
     */
    @Override
    public int getUniqueId() {
        String uniqueid = "";
        for (Integer i : this.getAddresses()) {
            uniqueid += i.toString();
        }

        return uniqueid.hashCode();
    }

    /*
     * 
     */
    public String getRepresentation() {

        // addresses
        String ret = "Sequence occurs at = [ ";
        ret += this.getAddressListRepresentation();

        // instructions
        for (Instruction inst : this.instlist) {
            ret += inst.getRepresentation() + "\n";
        }

        return ret;
    }
}

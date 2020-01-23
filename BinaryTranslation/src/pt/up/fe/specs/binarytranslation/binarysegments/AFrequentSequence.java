package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.Iterator;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public abstract class AFrequentSequence extends ABinarySegment {

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    protected AFrequentSequence(List<Instruction> ilist, List<SegmentContext> contexts) {
        super();
        this.instlist = ilist;
        this.contexts = contexts;

        // merge duplicate contexts
        for (SegmentContext contexta : this.contexts) {

            Iterator<SegmentContext> it = this.contexts.iterator();
            while (it.hasNext()) {
                var contextb = it.next();
                if (contexta == contextb)
                    continue;

                else if (contexta.equals(contextb))
                    contexta.merge(contextb);
            }
        }

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

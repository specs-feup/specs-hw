package pt.up.fe.specs.binarytranslation.tracer;

import java.util.List;

public class TraceBasicBlock extends ATraceUnit {

    private List<TraceInstruction> tilist;

    /*
    public TraceBasicBlock(List<TraceInstruction> tilist) {
        super(TraceUnitType.TraceBasicBlock);
        this.tilist = tilist;
    }*/

    public TraceBasicBlock(List<TraceInstruction> tilist, TraceUnitType bbtype) {
        super(bbtype);
        this.tilist = tilist;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (var tinst : this.tilist) {
            hash += tinst.hashCode();
        }
        return hash; // TODO: works??
    }

    @Override
    public String toString() {
        var bld = new StringBuilder();
        for (var tinst : this.tilist) {
            bld.append(tinst.toString() + "\n");
        }
        return bld.toString();
    }

    /*
     * basic block can be defined by start and end 
     * (do I need the end???)
     
    @Override
    public int getHash() {
        var bld = new StringBuilder();
        bld.append(this.getStart().getAddress().toString());
        bld.append(this.getEnd().getAddress().toString());
    
        // TODO: replace with something better?
        // this hash isnt sensitive to operand abstraction (should it be)?
        return bld.toString().hashCode();
    }
    */
}

package pt.up.fe.specs.binarytranslation.tracer;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class StreamSuperBlock extends AStreamUnit {

    private List<StreamBasicBlock> tbblist;

    /*
     * Deep Copy
     */
    public StreamSuperBlock(StreamSuperBlock other) {
        super(other);
        this.tbblist = new ArrayList<StreamBasicBlock>();
        for (var el : other.getList())
            this.tbblist.add(new StreamBasicBlock(el));
    }

    /*
     * Deep copy
     */
    @Override
    public StreamSuperBlock deepCopy() {
        return new StreamSuperBlock(this);
    }

    /*
     * User by copy constructor
     */
    protected List<StreamBasicBlock> getList() {
        return this.tbblist;
    }

    public StreamSuperBlock(List<StreamBasicBlock> tbblist) {
        super(StreamUnitType.StreamSuperBlock,
                tbblist.get(tbblist.size() - 1).getTargetAddr());
        this.tbblist = tbblist;
    }

    // used in map comparisons
    @Override
    public int hashCode() {
        int hash = 0;
        for (var tbb : this.tbblist) {
            hash += tbb.hashCode();
        }
        return hash; // TODO: works??
    }

    @Override
    public String toString() {
        var bld = new StringBuilder();
        for (var bb : this.tbblist) {
            bld.append(bb.toString());
        }
        return bld.toString();
    }

    @Override
    public Instruction getStart() {
        return this.tbblist.get(0).getStart();
    }

    @Override
    public Instruction getEnd() {
        return this.tbblist.get(this.tbblist.size() - 1).getEnd();
    }

    @Override
    public boolean containsAddr(Long addr) {
        for (var tbb : this.tbblist) {
            if (tbb.containsAddr(addr))
                return true;
        }
        return false;
    }

    /*
     * True if any instruction in this TraceUnit
     * includes the target of the "other"
     */
    @Override
    public boolean includesTarget(StreamUnit other) {
        for (var tbb : this.tbblist) {
            if (tbb.includesTarget(other))
                return true;
        }
        return false;
    }
}

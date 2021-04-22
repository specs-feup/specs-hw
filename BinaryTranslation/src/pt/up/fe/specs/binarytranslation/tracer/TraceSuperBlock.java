package pt.up.fe.specs.binarytranslation.tracer;

import java.util.List;

public class TraceSuperBlock extends ATraceUnit {

    private List<TraceBasicBlock> tbblist;

    public TraceSuperBlock(List<TraceBasicBlock> tbblist) {
        super(TraceUnitType.TraceSuperBlock);
        this.tbblist = tbblist;
    }

    @Override
    public int getHash() {
        int hash = 0;
        for (var tbb : this.tbblist) {
            hash += tbb.getHash();
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
}

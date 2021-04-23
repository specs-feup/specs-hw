package pt.up.fe.specs.binarytranslation.tracer;

public abstract class ATraceUnit implements TraceUnit {

    private final TraceUnitType type;

    public ATraceUnit(TraceUnitType type) {
        this.type = type;
    }

    @Override
    public TraceUnitType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    // TODO: if the detector wishes to determine equality based on
    // relative operands (i.e. symbolification), then it can
    // query the TraceUnit for a full list, and do whatever
    // transformations it requires
    //
    // for trace detectors, its just a matter of
    // directly calling the TraceUnit hash, which is
    // just addr based

    /*
    @Override
    public List<Instruction> getList() {
        return ilist;
    }
    
    @Override
    public Instruction getStart() {
        return ilist.get(0);
    }*/

    /*
    @Override
    public int getHash() {
        // TODO replace with something better?
        var bld = new StringBuilder();
        for (var inst : this.ilist)
            bld.append(inst.getAddress().toString());
    
        // TODO: this hash isnt sensitive to operand abstraction (should it be)?
        return bld.toString().hashCode();
    }*/
}

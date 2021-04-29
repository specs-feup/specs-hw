package pt.up.fe.specs.binarytranslation.tracer;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public interface StreamUnit {

    /*
     * 
     */
    public StreamUnitType getType();

    /*
     * 
     */
    public Instruction getStart();

    /*
     * 
     */
    public Instruction getEnd();

    /*
     * 
     */
    public Long getTargetAddr();

    /*
     * True if addresses of two units follow
     * i.e. if "this" comes after "other" 
     */
    public boolean follows(StreamUnit other);

    /*
     * True if addresses of two units follow
     * i.e. if "other" comes after "this"
     */
    public boolean precedes(StreamUnit other);

    /*
     * True if any instruction in this TraceUnit
     * includes the target of the "other"
     */
    public boolean includesTarget(StreamUnit other);

    /*
     * True if "this" jumps to "other"
     */
    public boolean jumpsTo(StreamUnit other);

    /*
     * True if "other" jumps to "this"
     */
    public boolean targetOf(StreamUnit other);
}

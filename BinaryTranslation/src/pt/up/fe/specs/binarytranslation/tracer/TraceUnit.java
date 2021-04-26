package pt.up.fe.specs.binarytranslation.tracer;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public interface TraceUnit {

    /*
     * 
     */
    public TraceUnitType getType();

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
     * 
     
    public List<Instruction> getList();*/
}

package pt.up.fe.specs.binarytranslation.generic;

import java.util.List;

import pt.up.fe.specs.binarytranslation.InstructionType;

public class InstructionData {

    protected String plainname;
    protected int latency;
    protected int delay;
    protected List<InstructionType> genericType;
    protected List<GenericInstructionOperand> operands;

    public InstructionData(String plainname, int latency, int delay, List<InstructionType> genericType,
            List<GenericInstructionOperand> operands) {
        this.plainname = plainname;
        this.latency = latency;
        this.delay = delay;
        this.genericType = genericType;
        this.operands = operands;
    }

    /*
     * Get plain name
     */
    public String getPlainName() {
        return this.plainname;
    }

    /*
     * Get latency
     */
    public int getLatency() {
        return this.latency;
    }

    /*
     * get delay
     */
    public int getDelay() {
        return this.delay;
    }

    /*
     * Get all generic types of this instruction
     */
    public List<InstructionType> getGenericTypes() {
        return this.genericType;
    }
}

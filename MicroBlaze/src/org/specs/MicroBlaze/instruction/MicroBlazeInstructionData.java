package org.specs.MicroBlaze.instruction;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldData;

import pt.up.fe.specs.binarytranslation.instruction.InstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;

public class MicroBlazeInstructionData extends InstructionData {

    /*
     * Only public constructor
     */
    public MicroBlazeInstructionData(InstructionProperties props, MicroBlazeAsmFieldData fieldData) {
        super(props);
        this.operands = fieldData.getOperands(props);
    }

    /*
     * Helper constructor for copy, calls super copy
     
    private MicroBlazeInstructionData(String plainname,
            int latency, int delay, List<InstructionType> genericType, List<Operand> ops) {
        super(plainname, latency, delay, genericType, ops);
    }*/

    /*
     * Helper constructor for copy, calls super copy
     */
    private MicroBlazeInstructionData(MicroBlazeInstructionData other) {
        super(other);
    }

    /*
     * Copy "constructor"
     */
    public MicroBlazeInstructionData copy() {
        return new MicroBlazeInstructionData(this);
        /*String copyname = new String(this.plainname);
        List<InstructionType> copytype = new ArrayList<InstructionType>(this.genericType);
        
        List<Operand> copyops = new ArrayList<Operand>();
        for (Operand op : this.operands)
            copyops.add(op.copy());
        
        return new MicroBlazeInstructionData(copyname, this.latency, this.delay, copytype, copyops);
        */

    }
}

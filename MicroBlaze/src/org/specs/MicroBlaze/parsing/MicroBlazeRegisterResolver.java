package org.specs.MicroBlaze.parsing;

import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.IMM;

import java.util.HashMap;
import java.util.Map;

import org.specs.MicroBlaze.asm.MicroBlazeRegister;
import org.specs.MicroBlaze.instruction.MicroBlazeRegisterDump;

import pt.up.fe.specs.binarytranslation.instruction.register.ExecutedImmediate;
import pt.up.fe.specs.binarytranslation.instruction.register.ExecutedRegister;

/**
 * 
 * @author nuno
 *
 */
public class MicroBlazeRegisterResolver {

    private final boolean postedImm;
    private final int upper16Imm;
    private final MicroBlazeAsmFieldData fielddata;
    private final MicroBlazeRegisterDump registers;

    // map of register definitions to values
    // For the Microblaze, since there are only 32 registers, an integer
    // is enough to decode the asmfield value into a register definition (e.g MicroBlazeRegister.R1)
    private final static Map<Integer, MicroBlazeRegister> intsToRegDefs;
    static {
        var amap = new HashMap<Integer, MicroBlazeRegister>();
        int i = 0;
        for (var regdef : MicroBlazeRegister.values())
            amap.put(i++, regdef);
        intsToRegDefs = amap;
    }

    public MicroBlazeRegisterResolver(
            MicroBlazeAsmFieldData fielddata,
            MicroBlazeRegisterDump registers,
            boolean postedImm, int upper16Imm) {
        this.fielddata = fielddata;
        this.registers = registers;
        this.postedImm = postedImm;
        this.upper16Imm = upper16Imm;
    }

    public MicroBlazeAsmFieldData getFielddata() {
        return fielddata;
    }

    public MicroBlazeRegisterDump getRegisters() {
        return registers;
    }

    private int getFullIMM(int lower16) {

        int fullImm = 0;

        // sign extend if no posted IMM
        if (postedImm == false) {
            fullImm = (lower16 << (16)) >> (16);
        }

        // else combine (assume upper16Imm already shifted up 16 bits)
        else {
            fullImm = upper16Imm | lower16;
        }

        return fullImm;
    }

    private int getFullIMMValue() {
        var lower16 = this.getFielddata().getMap().get(IMM);
        var fullImm = this.getFullIMM(lower16);
        return fullImm;
    }

    public ExecutedRegister resolve(MicroBlazeAsmField fieldName) {
        var map = fielddata.getMap();
        var fieldIntvalue = map.get(fieldName); // e.g. "RA("registera") -> 00010", I look up the binary number value in
                                                // the instruction via the fieldname in the parser, i.e, "registera",
                                                // which is encoded in the MicroBlazeAsmField enum
        var regDef = intsToRegDefs.get(fieldIntvalue); // I turn the number, 00010, into a register definition, as
                                                       // defined in MicroBlazeRegister, e.g., for 00010 --> R2("r2");
        var dataValue = registers.getValue(regDef);
        return new ExecutedRegister(fieldName, regDef, dataValue);
    }

    public ExecutedImmediate resolveFullIMM() {
        // var lower16 = this.getFielddata().getMap().get(IMM);
        // var fullImm = this.getFullIMM(lower16);
        return new ExecutedImmediate(MicroBlazeAsmField.IMM, getFullIMMValue());
    }

    public ExecutedImmediate resolveIMM() {
        return new ExecutedImmediate(MicroBlazeAsmField.IMM, fielddata.getMap().get(IMM));
    }
}

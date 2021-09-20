package org.specs.MicroBlaze.parsing.getters;

import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.specs.MicroBlaze.instruction.MicroBlazeRegisterDump;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldData;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType;
import org.specs.MicroBlaze.parsing.MicroBlazeRegisterResolver;

public class MicroBlazeAsmBranchGetter {

    /*
     * map TYPE to a specific private branch target getter func
     */
    interface MicroBlazeAsmBranchParse {
        Number apply(MicroBlazeRegisterResolver resolver);;
    }

    private static final Map<MicroBlazeAsmFieldType, MicroBlazeAsmBranchParse> TARGETGET;
    static {
        var amap = new HashMap<MicroBlazeAsmFieldType, MicroBlazeAsmBranchParse>();
        amap.put(MicroBlazeAsmFieldType.ULBRANCH, MicroBlazeAsmBranchGetter::rabranch);
        amap.put(MicroBlazeAsmFieldType.UBRANCH, MicroBlazeAsmBranchGetter::rabranch);
        amap.put(MicroBlazeAsmFieldType.UILBRANCH, MicroBlazeAsmBranchGetter::raibranch);
        amap.put(MicroBlazeAsmFieldType.UIBRANCH, MicroBlazeAsmBranchGetter::raibranch);
        // all of these can be absolute or relative (pc = register / imm, OR, pc = pc + register / imm)

        amap.put(MicroBlazeAsmFieldType.CBRANCH, MicroBlazeAsmBranchGetter::cbranch);
        amap.put(MicroBlazeAsmFieldType.CIBRANCH, MicroBlazeAsmBranchGetter::cibranch);
        // conditionals are alwayas relative (pc = pc + register/imm)

        amap.put(MicroBlazeAsmFieldType.RETURN, MicroBlazeAsmBranchGetter::rets);

        TARGETGET = Collections.unmodifiableMap(amap);
    }

    public static Number getFrom(MicroBlazeAsmFieldData fielddata, MicroBlazeRegisterDump registers) {

        var func = TARGETGET.get(fielddata.get(MicroBlazeAsmFieldData.TYPE));
        if (func == null)
            func = MicroBlazeAsmBranchGetter::undefined;

        var postedImm = MicroBlazeAsmOperandGetter.getPostedIMM();
        var upper16Imm = MicroBlazeAsmOperandGetter.getUpper16IMM();
        var resolver = new MicroBlazeRegisterResolver(fielddata, registers, postedImm, upper16Imm);
        return func.apply(resolver);
    }

    ///////////////////////////////////////////////////////////////////////
    private static Number rabranch(MicroBlazeRegisterResolver resolver) {
        var rvalue = resolver.resolve(RB).getDataValue().intValue();
        var map = resolver.getFielddata().getMap();
        var absolute = (map.get(OPCODEA) & 0b01) != 0; // A bit in all unconditional jumps
        int target = 0;
        if (absolute) {
            target = rvalue;
        }

        else {
            target = resolver.getFielddata().getAddr().intValue() + rvalue;
        }
        return target;
    }

    ///////////////////////////////////////////////////////////////////////
    private static Number raibranch(MicroBlazeRegisterResolver resolver) {
        var immtarget = resolver.resolveFullIMM().getDataValue().intValue();
        var map = resolver.getFielddata().getMap();
        var absolute = (map.get(OPCODEA) & 0b01) != 0; // A bit in all unconditional jumps
        int target = 0;
        if (absolute) {
            target = immtarget;
        }

        else {
            target = resolver.getFielddata().getAddr().intValue() + immtarget;
        }
        return target;
    }

    ///////////////////////////////////////////////////////////////////////
    private static Number cbranch(MicroBlazeRegisterResolver resolver) {
        var rvalue = resolver.resolve(RB).getDataValue().intValue();
        return Long.valueOf(resolver.getFielddata().getAddr().intValue() + rvalue);
    }

    ///////////////////////////////////////////////////////////////////////
    private static Number cibranch(MicroBlazeRegisterResolver resolver) {
        var immtarget = resolver.resolveFullIMM().getDataValue().intValue();
        return Long.valueOf(resolver.getFielddata().getAddr().intValue() + immtarget);
    }

    // TODO: returns are relative and use the contents of register Ra
    ///////////////////////////////////////////////////////////////////////
    private static Number rets(MicroBlazeRegisterResolver resolver) {
        var immtarget = resolver.resolveFullIMM().getDataValue().intValue();
        return Long.valueOf(resolver.getFielddata().getAddr().intValue() + immtarget);
    }

    ///////////////////////////////////////////////////////////////////////
    private static Number undefined(MicroBlazeRegisterResolver resolver) {
        return null;
    }
}

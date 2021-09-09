package org.specs.MicroBlaze.parsing.getters;

import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldData;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType;

import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;

public class MicroBlazeAsmBranchGetter {

    /*
     * map TYPE to a specific private branch target getter func
     */
    interface MicroBlazeAsmBranchParse {
        Number apply(MicroBlazeAsmFieldData fielddata, List<Operand> operands);
    }

    /*
    @Override
    public Number getBranchTarget() {
        if (this.isJump()) {
            var numops = this.getData().getOperands().size();
            var jmpval = this.getData().getOperands().get(numops - 1).getNumberValue();
    
            long finalvalue = 0;
            if (this.isRelativeJump())
                finalvalue = (this.getAddress().longValue() + jmpval.longValue());
            else
                finalvalue = jmpval.longValue();
    
            return finalvalue;
    
            // TODO replace mask with mask built based on elf instruction width
            // or info about instruction set
        }
    
        return null;
    }*/

    private static final Map<MicroBlazeAsmFieldType, MicroBlazeAsmBranchParse> TARGETGET;
    static {
        var amap = new HashMap<MicroBlazeAsmFieldType, MicroBlazeAsmBranchParse>();
        amap.put(MicroBlazeAsmFieldType.ULBRANCH, MicroBlazeAsmBranchGetter::rabranch);
        amap.put(MicroBlazeAsmFieldType.UBRANCH, MicroBlazeAsmBranchGetter::rabranch);

        amap.put(MicroBlazeAsmFieldType.UILBRANCH, MicroBlazeAsmBranchGetter::rabranch);
        amap.put(MicroBlazeAsmFieldType.UIBRANCH, MicroBlazeAsmBranchGetter::rabranch);
        // NOTE: only UIBRANCH and UILBRANCH types are capable of relative OR absolute jumps
        // all other branches are relative
        // WRONG, see next comment

        amap.put(MicroBlazeAsmFieldType.CBRANCH, MicroBlazeAsmBranchGetter::cbranch);
        amap.put(MicroBlazeAsmFieldType.CIBRANCH, MicroBlazeAsmBranchGetter::cibranch);
        // Only conditionals can be relative

        amap.put(MicroBlazeAsmFieldType.RETURN, MicroBlazeAsmBranchGetter::rets);

        TARGETGET = Collections.unmodifiableMap(amap);
    }

    private static int getFullIMM(MicroBlazeAsmFieldData fielddata) {

        var map = fielddata.getMap();
        int fullimm = 0;
        var lower16 = map.get(IMM);
        var isUpperImm = MicroBlazeAsmOperandGetter.isPostedImm();

        // sign extend if no posted IMM
        if (isUpperImm == false) {
            fullimm = (lower16 << (16)) >> (16);
        }

        // else combine (assume upper16Imm already shifted up 16 bits)
        else {
            fullimm = MicroBlazeAsmOperandGetter.getUpper16Imm() | lower16;
        }

        return fullimm;
    }

    public static Number getFrom(MicroBlazeAsmFieldData fielddata, List<Operand> operands) {

        var func = TARGETGET.get(fielddata.get(MicroBlazeAsmFieldData.TYPE));
        if (func == null)
            func = MicroBlazeAsmBranchGetter::undefined;

        return func.apply(fielddata);
    }

    ///////////////////////////////////////////////////////////////////////
    private static Number cbranch(MicroBlazeAsmFieldData fielddata, List<Operand> operands) {

        // TODO: target is sum of PC + rb

        return 0;
    }

    ///////////////////////////////////////////////////////////////////////
    private static Number cibranch(MicroBlazeAsmFieldData fielddata, List<Operand> operands) {
        var target = MicroBlazeAsmBranchGetter.getFullIMM(fielddata);
        return Long.valueOf(fielddata.getAddr().longValue() + target);
    }

    ///////////////////////////////////////////////////////////////////////
    private static Number rabranch(MicroBlazeAsmFieldData fielddata, List<Operand> operands) {
        var target = MicroBlazeAsmBranchGetter.getFullIMM(fielddata);
        var map = fielddata.getMap();
        if (map.containsKey(OPCODEA)) {
            var absolute = map.get(OPCODEA) & 0b01; // A bit in all unconditional jumps
            
        } else
            //return Long.valueOf(fielddata.getAddr().longValue() + target);
            
            //TODO: wrong, "brad" is also absolute and is format UBRANCH 
            
            // but the A bit seems to be in the same place for all families?? except "brk" which doesnt have OPCODEA field
    }

    // TODO: returns are relative and use the contents of register Ra
    ///////////////////////////////////////////////////////////////////////
    private static Number rets(MicroBlazeAsmFieldData fielddata, List<Operand> operands) {

        return 0;
    }

    ///////////////////////////////////////////////////////////////////////
    private static Number undefined(MicroBlazeAsmFieldData fielddata, List<Operand> operands) {
        return null;
    }
}

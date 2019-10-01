package org.specs.MicroBlaze.asmparser;

import static org.specs.MicroBlaze.asmparser.MicroBlazeAsmInstructionType.*;
import static pt.up.fe.specs.binarytranslation.asmparser.binaryasmparser.BinaryAsmInstructionParser.newInstance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionParser;
import pt.up.fe.specs.binarytranslation.asmparser.IsaParser;
import pt.up.fe.specs.binarytranslation.generic.GenericIsaParser;
import pt.up.fe.specs.util.SpecsSystem;

public interface MicroBlazeInstructionParsers {

    List<AsmInstructionParser> PARSERS = Arrays.asList(
            newInstance(SPECIAL, "100101_0x000xx00xxx_0(14)"),
            newInstance(UBRANCH, "101110_0(5)_opcode(5)_imm(16)"),
            newInstance(CBRANCH, "101111_opcode(5)_0(5)_imm(16)"),
            newInstance(BARREL, "010001_0(14)_xx_0(10)"),
            newInstance(IBARREL, "011001_0(6)_xx00xx_0(10)"),
            newInstance(STREAM, "011011_0(10)_x_0(15)"),
            newInstance(DSTREAM, "010011_0(10)_x_0(15)"),
            newInstance(TYPE_A, "xx0xxx_rd(5)_ra(5)_rb(5)_x(11)"),
            newInstance(TYPE_B, "xx1xxx_rd(5)_ra(5)_x(16)"),
            newInstance(UNDEFINED, "x(32)"));

    static IsaParser getMicroBlazeIsaParser() {
        Set<String> allowedFields = new HashSet<>(SpecsSystem.getStaticFields(MicroBlazeAsmFields.class, String.class));
        return new GenericIsaParser(PARSERS, allowedFields);
    }
}

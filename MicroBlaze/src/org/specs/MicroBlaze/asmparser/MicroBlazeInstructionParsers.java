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
            newInstance(SPECIAL, "100101_opcodea(5)_opcodeb(5)_opcodec(2)_sdsa(14)"),
            newInstance(UBRANCH, "101110_0(5)_opcodea(5)_imm(16)"),
            newInstance(CBRANCH, "101111_opcodea(5)_0(5)_imm(16)"),
            newInstance(BARREL, "010001_rd(5)_ra(5)_rb(5)_opcodea(2)_0(14)"),
            newInstance(IBARREL, "011001_rd(5)_ra(5)_opcodea(2)_000_opcodeb(5)_0_imm(5)"),
            newInstance(STREAM, "011011_rd(5)_ra(5)_direction(1)_0(15)"),
            newInstance(DSTREAM, "010011_rd(5)_ra(5)_direction(1)_0(15)"),
            newInstance(TYPE_A, "opcodea(2)_0_opcodeb(3)_rd(5)_ra(5)_rb(5)_opcodec(11)"),
            newInstance(TYPE_B, "opcodea(2)_1_opcodeb(3)_rd(5)_ra(5)_imm(16)"),
            newInstance(UNDEFINED, "x(32)"));

    static IsaParser getMicroBlazeIsaParser() {
        Set<String> allowedFields = new HashSet<>(SpecsSystem.getStaticFields(MicroBlazeAsmFields.class, String.class));
        return new GenericIsaParser(PARSERS, allowedFields);
    }
}

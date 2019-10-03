package org.specs.MicroBlaze.isa;

import static org.specs.MicroBlaze.isa.MicroBlazeInstructionType.*;
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
            newInstance(SPECIAL, "100101_opcodea(5)_opcodeb(5)_opcodec(2)_opcoded(14)"),
            newInstance(UBRANCH, "10_opcodea(1)_110_registerd(5)_opcodeb(5)_imm(16)"),
            newInstance(CBRANCH, "10_opcodea(1)_111_opcodeb(5)_registera(5)_imm(16)"),
            newInstance(RETURN, "101101_opcodea(5)_registera(5)_imm(16)"),
            newInstance(IBARREL, "011001_registerd(5)_registera(5)_opcodea(2)_000_opcodeb(5)_0_imm(5)"),
            newInstance(STREAM, "011011_registerd(5)_registera(5)_opcodea(1)_0(15)"),
            newInstance(DSTREAM, "010011_registerd(5)_registera(5)_opcodea(1)_0(15)"),
            newInstance(TYPE_A, "opcodea(2)_0_opcodeb(3)_registerd(5)_registera(5)_registerb(5)_opcodec(11)"),
            newInstance(TYPE_B, "opcodea(2)_1_opcodeb(3)_registerd(5)_registera(5)_imm(16)"),
            newInstance(UNDEFINED, "x(32)"));

    static IsaParser getMicroBlazeIsaParser() {
        Set<String> allowedFields = new HashSet<>(
                SpecsSystem.getStaticFields(MicroBlazeInstructionParsersFields.class, String.class));
        return new GenericIsaParser(PARSERS, allowedFields);
    }
}

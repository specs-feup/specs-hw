package org.specs.Arm.asm;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.specs.Arm.isa.ArmInstruction;

public class ArmInstructionInstatiationTest {

    /*
     * Instantiate one of every single instruction in the ISA as specificied by the manual:
     * https://static.docs.arm.com/ddi0487/ea/DDI0487E_a_armv8_arm.pdf
     * To see if they get correctly identified into the proper mnemonic
     */
    @Test
    public void test() {

        Map<String, List<String>> instlist = new LinkedHashMap<String, List<String>>();

        // DPI_PCREL //////////////////////////////////////////////////////////
        instlist.put("adr",
                Arrays.asList("adr", Integer.toHexString(0b000_10000_0000000000000000000_00000)));

        instlist.put("adrp",
                Arrays.asList("adrp", Integer.toHexString(0b100_10000_0000000000000000000_00000)));

        // DPI_ADDSUBIMM //////////////////////////////////////////////////////
        // 32 bit add
        instlist.put("add_imm_32",
                Arrays.asList("add", Integer.toHexString(0b000_100010_0_000000000000_00000_00000)));

        // 32 bit add w/set flag
        instlist.put("adds_imm_32",
                Arrays.asList("adds", Integer.toHexString(0b001_100010_0_000000000000_00000_00000)));

        // 32 bit sub
        instlist.put("sub_imm_32",
                Arrays.asList("sub", Integer.toHexString(0b010_100010_0_000000000000_00000_00000)));

        // 32 bit sub w/set flag
        instlist.put("subs_imm_32",
                Arrays.asList("subs", Integer.toHexString(0b011_100010_0_000000000000_00000_00000)));

        // 64 bit add
        instlist.put("add_imm_64",
                Arrays.asList("add", Integer.toHexString(0b100_100010_0_000000000000_00000_00000)));

        // 64 bit add w/set flag
        instlist.put("adds_imm_64",
                Arrays.asList("adds", Integer.toHexString(0b101_100010_0_000000000000_00000_00000)));

        // 64 bit sub
        instlist.put("sub_imm_64",
                Arrays.asList("sub", Integer.toHexString(0b110_100010_0_000000000000_00000_00000)));

        // 64 bit sub w/set flag
        instlist.put("subs_imm_64",
                Arrays.asList("subs", Integer.toHexString(0b111_100010_0_000000000000_00000_00000)));

        // DPI_ADDSUBIMM_TAGS /////////////////////////////////////////////////
        // add with tags
        instlist.put("addg",
                Arrays.asList("addg", Integer.toHexString(0b100_100011_0_000000000000_00000_00000)));

        // sub with tags
        instlist.put("subg",
                Arrays.asList("subg", Integer.toHexString(0b110_100011_0_000000000000_00000_00000)));

        // LOGICAL ////////////////////////////////////////////////////////////
        // logical and immediate 32 bits
        instlist.put("and_imm_32",
                Arrays.asList("and", Integer.toHexString(0b000_100100_0_000000000000_00000_00000)));

        // logical orr immediate 32 bits
        instlist.put("orr_imm_32",
                Arrays.asList("orr", Integer.toHexString(0b001_100100_0_000000000000_00000_00000)));

        // logical eor immediate 32 bits
        instlist.put("eor_imm_32",
                Arrays.asList("eor", Integer.toHexString(0b010_100100_0_000000000000_00000_00000)));

        // logical ands immediate 32 bits
        instlist.put("ands_imm_32",
                Arrays.asList("ands", Integer.toHexString(0b011_100100_0_000000000000_00000_00000)));

        // logical and immediate 64 bits
        instlist.put("and_imm_64",
                Arrays.asList("and", Integer.toHexString(0b100_100100_0_000000000000_00000_00000)));

        // logical orr immediate 64 bits
        instlist.put("orr_imm_64",
                Arrays.asList("orr", Integer.toHexString(0b101_100100_0_000000000000_00000_00000)));

        // logical eor immediate 64 bits
        instlist.put("eor_imm_64",
                Arrays.asList("eor", Integer.toHexString(0b110_100100_0_000000000000_00000_00000)));

        // logical ands immediate 64 bits
        instlist.put("ands_imm_64",
                Arrays.asList("ands", Integer.toHexString(0b111_100100_0_000000000000_00000_00000)));

        // MOVEW //////////////////////////////////////////////////////////////

        var keys = instlist.keySet();
        for (String key : keys) {
            var values = instlist.get(key);
            ArmInstruction testinst = new ArmInstruction("0", values.get(1));
            System.out.print(key + "\tresolved to\t->\t" + testinst.getName() +
                    "\t(SIMD: " + testinst.isSimd() + ", width: " + testinst.getBitWidth() + ")\n");
            assertEquals(values.get(0), testinst.getName());
        }

    }
}

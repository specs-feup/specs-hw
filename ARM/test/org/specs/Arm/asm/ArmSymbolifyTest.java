package org.specs.Arm.asm;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Operand;

public class ArmSymbolifyTest {

    @Test
    public void test() {

        // aa0203f7
        // 1_010101_000_0_00010_000000_11111_10111
        // sf = 1
        // shift = 000
        // m = 2
        // imm = 0
        // n = 32 --> sp
        // d = 23
        // ArmInstruction i = ArmInstruction.newInstance("0", "aa0203f7");

        // 9277f821
        // and x1, x1, #0xfffffffffffffeff
        ArmInstruction i = ArmInstruction.newInstance("0", "9277f821");
        i.printInstruction();

        char c = 'a';
        Map<String, String> regremap = new HashMap<String, String>();
        for (Operand op : i.getData().getOperands()) {

            if (op.isSpecial())
                continue;

            if (regremap.containsKey(op.getRepresentation()))
                continue;

            regremap.put(op.getRepresentation(), op.getPossibleSymbolicRepresentation(String.valueOf(c)));
            c++;
        }

        //
        i.makeSymbolic(0, regremap);
        i.printInstruction();
    }

}

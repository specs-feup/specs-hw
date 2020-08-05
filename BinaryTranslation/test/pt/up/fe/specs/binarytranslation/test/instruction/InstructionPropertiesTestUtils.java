package pt.up.fe.specs.binarytranslation.test.instruction;

import static org.junit.Assert.assertEquals;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;

public class InstructionPropertiesTestUtils {

    /*
     * Test checks if the manually initialized data in the MicroBlazeInstructionProperties enum
     * matches with what the parsers decode when initializing the enum private fields
     */
    public static void validityTest(List<InstructionProperties> propList) {
        for (InstructionProperties props : propList) {
            var v1 = props.getCodeType().toString();
            var v2 = props.getFieldData().getType().toString();
            System.out.print(props.getEnumName() + ":\t\t" + v1 + "\tvs.\t" + v2 + "\n");
            assertEquals(v1, v2);
        }
    }

    /*
     * Test if all opcodes in all the instruction properties for the list are unique
     */
    public static void uniquenessTest(List<InstructionProperties> propList) {
        int matchcount = 0;
        for (InstructionProperties first : propList) {
            matchcount = 0;
            for (InstructionProperties second : propList) {
                if (first.getOpCode() == second.getOpCode())
                    matchcount++;
            }
            System.out.print("props: " + first.getName() + " matches = " + matchcount + "\n");
            assertEquals(matchcount, 1);
        }
    }
}

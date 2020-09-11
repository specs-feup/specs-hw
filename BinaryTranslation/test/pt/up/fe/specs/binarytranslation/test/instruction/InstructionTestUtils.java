package pt.up.fe.specs.binarytranslation.test.instruction;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;

public class InstructionTestUtils {

    /*
     * Test checks if the manually initialized data in the MicroBlazeInstructionProperties enum
     * matches with what the parsers decode when initializing the enum private fields
     */
    public static void validityTest(List<InstructionProperties> propList) {
        for (InstructionProperties props : propList) {

            // quick hack...
            if (props.getCodeType().toString().equals("UNDEFINED"))
                continue;

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
            System.out.println("----------------------------");
            for (InstructionProperties second : propList) {
                if (first.getOpCode() == second.getOpCode()) {
                    matchcount++;
                    System.out.println(first.getName() + " matches " + second.getName());
                }
            }
            System.out.println("props: " + first.getName() + " matches = " + matchcount);
            System.out.println("----------------------------");
            assertEquals(matchcount, 1);
        }
    }

    /*
     * Tests if decoding of instructions happens correctly 
     * (i.e. expected decode matches inst name given its encoding)
     */
    public static void instructionInstantiate(List<InstructionEncoding> encodeList, Class<?> instClass) {

        Method newInstMethod;
        try {
            newInstMethod = instClass.getMethod("newInstance", String.class, String.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        for (var inst : encodeList) {
            try {
                Instruction testinst = (Instruction) newInstMethod.invoke(null, "0", inst.getCode());
                assertEquals(inst.getName(), testinst.getName());
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }
}

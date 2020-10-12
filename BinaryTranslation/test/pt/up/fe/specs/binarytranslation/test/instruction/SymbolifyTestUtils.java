package pt.up.fe.specs.binarytranslation.test.instruction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.detection.detectors.BinarySegmentDetectionUtils;
import pt.up.fe.specs.binarytranslation.detection.detectors.HashedSequence;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class SymbolifyTestUtils {

    public static void symbolifyTest(Instruction i) {

        Method method = null;
        try {
            method = BinarySegmentDetectionUtils.class.getDeclaredMethod("hashSequence", List.class);
            method.setAccessible(true);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();

        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // hash instruction
        var l = Arrays.asList(i);
        HashedSequence hs = null;
        try {
            hs = (HashedSequence) method.invoke(BinarySegmentDetectionUtils.class, l);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // TEST THIS:
        hs.makeSymbolic();
    }
}

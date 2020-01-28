package org.specs.MicroBlaze.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.binarysegments.detection.BinarySegmentDetectionUtils;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.HashedSequence;

public class MicroBlazeSymbolifyTest {

    @Test
    public void test() {
        MicroBlazeInstruction i = MicroBlazeInstruction.newInstance("0", "3021ffe0"); // addik r1, r1, 0xffffffe0
        i.printInstruction();

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

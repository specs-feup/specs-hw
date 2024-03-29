/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.specs.MicroBlaze.test.stream;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;
import org.specs.MicroBlaze.provider.MicroBlazeZippedELFProvider;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTester;

/**
 * Trace printing test cases; txt files are used so that the backend tools can run on Jenkins without need for
 * installing GNU utils for the MicroBlaze architecture
 * 
 * @author nuno
 *
 */
public class MicroBlazeStreamTester extends InstructionStreamTester {

    final MicroBlazeZippedELFProvider elfUnderTest = MicroBlazeLivermoreN100.innerprod; // MicroBlazeRosetta.rendering3d;

    @Test
    public void testA() {
        try (var stream = elfUnderTest.toTraceStream()) {
            stream.runUntil(stream.getApp().getKernelStart());

            // 1st
            var inst = stream.nextInstruction();
            var map = inst.getRegisters().getRegisterMap();

            // for (var reg : MicroBlazeRegister.values())
            // System.out.println(reg.getName() + " = " + map.get(reg).longValue());

            // System.out.println(inst.getRegisters().getValue(MicroBlazeRegister.R3));

            for (var r : map.keySet())
                System.out.println(r.getName() + " = " + map.get(r).longValue());

            // 2nd
            inst = stream.nextInstruction();
            var ops = inst.getData().getOperands();
            System.out.println(inst.getRepresentation());
            for (var op : ops) {
                System.out.println(
                        "reg = " + op.getAsmField() + ", regc = "
                                + op.getName() + ", value = " + op.getDataValue());

                if (op.isRegister())
                    System.out.println("is temp = "
                            + op.getContainerRegister().getRegisterDefinition().isTemporary());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testStatic() {
        printStream(elfUnderTest.toStaticStream());
    }

    @Test
    public void testStaticRaw() {
        rawDump(elfUnderTest.toStaticStream());
    }

    @Test
    public void testStaticRawFromTxtDump() {
        rawDump(elfUnderTest.asTxtDump().toStaticStream());
    }

    @Test
    public void testTrace() {
        printStream(elfUnderTest.toTraceStream());
    }

    @Test
    public void testTraceRaw() {
        rawDump(elfUnderTest.toTraceStream());

        // TODO:
        /*0xd8 <precise_random_f32+216>:  0x30606b20
        ../../binutils-2_35/gdb/inline-frame.c:176: internal-error: void inline_frame_this_id(frame_info*, void**, frame_id*): Assertion `!frame_id_eq (*this_id, outer_frame_id)' failed.
        A problem internal to GDB has been detected,
        further debugging may prove unreliable.*/
    }

    @Test
    public void testTraceRawFromTxtDump() {
        rawDump(elfUnderTest.asTraceTxtDump().toTraceStream());
    }
}

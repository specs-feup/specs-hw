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
 
package org.specs.Riscv.test.stream;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTester;

public class RiscvStreamTester extends InstructionStreamTester {

    @Test
    public void testStatic() {
        printStream(RiscvLivermoreN100im.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRaw() {
        rawDump(RiscvLivermoreN100im.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRawFromTxtDump() {
        rawDump(RiscvLivermoreN100im.innerprod.asTxtDump().toStaticStream());
    }

    @Test
    public void testTrace() {
        printStream(RiscvLivermoreN100im.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRaw() {
        rawDump(RiscvLivermoreN100im.cholesky.toTraceStream());
    }

    @Test
    public void testTraceRawFromTxtDump() {
        rawDump(RiscvLivermoreN100im.innerprod.asTraceTxtDump().toTraceStream());
    }
}

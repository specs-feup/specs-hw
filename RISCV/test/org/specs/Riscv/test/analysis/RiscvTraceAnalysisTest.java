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
 
package org.specs.Riscv.test.analysis;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvGccELF;

public class RiscvTraceAnalysisTest {

    /*  private RiscvTraceStream getStream(ELFProvider file, boolean generate) {
        String f = file.getResource();
        if (!generate) {
            f = ((RiscvLivermoreELFN100iam) file).asTraceTxtDump();
        }
        File fd = SpecsIo.resourceCopy(f);
        fd.deleteOnExit();
        var prod = new RiscvDetailedTraceProvider(fd);
        var stream = new RiscvTraceStream(prod);
        return stream;
    }*/

    /*
    @Deprecated
    private RiscvTraceStream getStream(RiscvELFProvider elfprovider, boolean generate) {
        var prod = new RiscvDetailedTraceProducer(new RiscvTraceDumpProvider(elfprovider));
        var stream = new RiscvTraceStream(prod);
        return stream;
    }*/

    @Test
    public void testRiscvDetailedProvider() {
        // try (var prov = new RiscvDetailedTraceProducer(RiscvGccELF.autocor)) {

        try (var prov = RiscvGccELF.autocor.toTraceStream()) {
            var i = prov.nextInstruction();
            while (i != null) {
                i.printInstruction();
                i = prov.nextInstruction();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testRiscvInOuts() {
        // var elf = RiscvLivermoreELFN100iam.innerprod100;
        // var elf = RiscvGccELF.autocor;
        // var stream = getStream(elf, true);
        // var bba = new InOutAnalyzer(stream, elf);
        // bba.analyse(InOutMode.BASIC_BLOCK, 10);
    }
}

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
 
package org.specs.Riscv.test.detection;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;
import org.specs.Riscv.stream.RiscvTraceProducer;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.elf.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;

public class RiscvBatchFrequentSequenceTest {

    /*
     * Stats from static frequent sequences
     
    @Test
    public void RiscvFrequentSequenceDetect() {
        ELFProvider elfs[] = RiscvLivermoreELFN100iam.values();
        // ELFProvider elfs[] = { RiscvLivermoreELFN100iam.innerprod100 };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 2, 20,
                RiscvStaticProvider.class,
                RiscvElfStream.class,
                FrequentStaticSequenceDetector.class);
    }*/

    /*
     * Stats from trace basic blocks
     */
    @Test
    public void RiscvTraceBasicBlockDetect() {
        // ELFProvider elfs[] = RiscvLivermoreELFN100iam.values();
        ELFProvider elfs[] = { RiscvLivermoreN100im.pic2d };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 4, 5,
                RiscvTraceProducer.class,
                RiscvTraceStream.class,
                TraceBasicBlockDetector.class);
    }
}

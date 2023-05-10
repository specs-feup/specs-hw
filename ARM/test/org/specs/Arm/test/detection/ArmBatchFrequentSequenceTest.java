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
 
package org.specs.Arm.test.detection;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN100;
import org.specs.Arm.stream.ArmTraceProducer;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.elf.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;

public class ArmBatchFrequentSequenceTest {

    /*
     * Stats from static frequent sequences
     
    @Test
    public void ArmFrequentSequenceDetect() {
        ELFProvider elfs[] = ArmLivermoreELFN10.values();
        // ELFProvider elfs[] = { ArmLivermoreELFN10.innerprod100 };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 2, 20,
                ArmStaticProvider.class,
                ArmElfStream.class,
                FrequentStaticSequenceDetector.class);
    }
    */
    /*
     * Stats from trace basic blocks
     */
    @Test
    public void ArmTraceBasicBlockDetect() {

        // using the N10 version is fine, if we use the kernel start and stop bounds

        // ELFProvider elfs[] = ArmLivermoreELFN10.values();
        ELFProvider elfs[] = { ArmLivermoreN100.tridiag };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 4, 50,
                ArmTraceProducer.class,
                ArmTraceStream.class,
                TraceBasicBlockDetector.class);
    }
}

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
 
package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.utils.SlidingWindow;

public abstract class AFixedSizeFrequentSequenceDetector extends ASimpleSegmentDetector {

    /*
     * Constructor
     */
    public AFixedSizeFrequentSequenceDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * Check if candidate sequence is valid
     */
    @Override
    protected Boolean validSequence(SlidingWindow<Instruction> window) {

        // check precendent requirements first!
        if (super.validSequence(window) == false)
            return false;

        // check if this subsequence is at all apt
        for (Instruction inst : window.getWindow()) {

            // TODO fail with stream instructions

            // do not form sequences with unknown instructions
            // do not form frequent sequences containing jumps
            // TODO: TEMPORARY RESTRICTION ON MEMORY AND FLOAT FOR HDL GENERATION
            if (inst.isUnknown() || inst.isJump() || inst.isMemory() || inst.isFloat()) {
                return false;
            }
        }

        return true;
    }
}

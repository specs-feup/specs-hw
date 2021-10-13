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

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.utils.SlidingWindow;

/**
 * Basic block detector which only detects blocks of a specific size
 * 
 * @author nuno
 *
 */
public abstract class AFixedSizeBasicBlockDetector extends ASimpleSegmentDetector {

    /*
     * Constructor
     */
    public AFixedSizeBasicBlockDetector(DetectorConfiguration config) {
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

        // must obey these conditions

        // have only one branch
        int bcount = 0;
        for (var i : window.getWindow()) {
            if (i.isJump())
                bcount++;
        }
        if (bcount > 1)
            return false;

        // check jump to start
        Instruction last = null;
        if (this.getCurrentStream().getApp()
                .get(Application.CPUNAME)
                .equals("microblaze32")) // NOTE: this is a quick hack

            if (window.getFromLast(1).isBackwardsJump()) // still a bug here, if a branch without delay is second to
                                                         // last
                last = window.getFromLast(1);
            else
                last = window.getLast();
        else
            last = window.getLast(); // TODO: doesn't work for microblaze due to delay slots...

        if (!(last.isBackwardsJump() && last.isConditionalJump() && last.isRelativeJump()))
            return false;

        // target isn't start of window, skip this candidate
        var targetAddr = last.getData().getBranchTarget().longValue();// last.getBranchTarget().longValue(); // TODO
                                                                      // what if branch is based on register values?
        var firstAddr = window.get(0).getAddress().longValue();
        if (targetAddr != firstAddr)
            return false;

        return true;
    }
}

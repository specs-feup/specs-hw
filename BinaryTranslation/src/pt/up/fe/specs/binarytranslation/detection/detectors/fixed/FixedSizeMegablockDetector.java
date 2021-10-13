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

import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.MegaBlock;
import pt.up.fe.specs.binarytranslation.detection.segments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.utils.SlidingWindow;

public class FixedSizeMegablockDetector extends ASimpleSegmentDetector {

    /*
     * Constructor
     */
    public FixedSizeMegablockDetector(DetectorConfiguration config) {
        super(config);
    }

    @Override
    protected BinarySegment makeSegment(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new MegaBlock(symbolicseq, contexts, this.getCurrentStream().getApp());
    }

    // TODO: megablocks and other trace based detections do not benefit from
    // register remapping in hashSequene function...

    // TODO: sliding window should be sorted by addr, to prevent the same
    // megablock from being detected as different megablocks
    // NO. even if i sort the window, this is incorrect, as the instructions
    // execute, obviously the window shifts (througout the iteration execution)
    // this doesnt mean that a sequence such as <addr0, addr1, addr2, addr4, ..., branch to addr0>
    // has executed twice, if I count sequence if I detect <addr1, addr2, addr4, ..., branch to addr0, addr0>
    // and then sort it!
    //
    // the problem with the hash based detection im doing is that while basic blocks have
    // validity conditions that allow for "halfway" iterations to be discarded as invalid
    // there is no such thing with the megablock, since it can end anywhere, UNLESS
    // I enforce a branch to the beginning....

    // TODO: this has another problem: a fixed sized megablock detector will be
    // very inefficient... maybe its best to return to the variable sized
    // versions, but a pre-processing step returns a sublist with a variable sized valid candidate??

    /*
     * 
     */
    @Override
    protected Boolean validSequence(SlidingWindow<Instruction> window) {

        // check precendent requirements first!
        if (super.validSequence(window) == false)
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
        //var targetAddr = last.getBranchTarget().longValue(); // TODO what if branch is based on register values?
        var targetAddr = last.getData().getBranchTarget().longValue();
        var firstAddr = window.get(0).getAddress().longValue();
        if (targetAddr != firstAddr)
            return false;

        return true;
    }
}

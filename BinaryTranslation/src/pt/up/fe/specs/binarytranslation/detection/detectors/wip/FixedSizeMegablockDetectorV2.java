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
 
package pt.up.fe.specs.binarytranslation.detection.detectors.wip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.detectors.ASegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.HashedSequence;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.MegaBlock;
import pt.up.fe.specs.binarytranslation.detection.segments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnit;
import pt.up.fe.specs.binarytranslation.utils.SlidingWindow;

/**
 * 
 * @author nuno
 *
 */
public class FixedSizeMegablockDetectorV2 extends ASegmentDetector {

    public FixedSizeMegablockDetectorV2(DetectorConfiguration config) {
        super(config);
    }

    @Override
    protected BinarySegment makeSegment(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new MegaBlock(symbolicseq, contexts, this.getCurrentStream().getApp());
    }

    /*
     * Get the pair which represent the start and end of a block which
     * can contain any number of backwards or forwards branches, but ends in a backwards branch
     */
    private StreamUnit getCandidate(InstructionStream istream, SlidingWindow<Instruction> window) {

        if (!istream.hasNext())
            return null;

        // find next jump
        Instruction previs = window.add(istream.nextInstruction()), is = null;
        while ((is = istream.peekNext()) != null) {
            var addr1 = previs.getAddress();
            var addr2 = is.getAddress();

            // if branch was backwards, and target within scan window, and target == is
            if ((addr2 < addr1) && window.exists(addr2)) {
                var targetis = window.getLatestByAddr(addr2);
                if (addr2.compareTo(targetis.getAddress()) == 0)
                    return new StreamUnit(targetis, previs);
            }

            // advance
            previs = window.add(istream.nextInstruction());
        }

        return null;
    }

    /*
     * Expands the TraceUnits into full instruction list
     */
    private List<Instruction> expandTraceUnits(List<StreamUnit> units, SlidingWindow<Instruction> window) {

        var fullList = new ArrayList<Instruction>();
        for (var unit : units)
            fullList.addAll(window.getRange(unit.getStart(), unit.getEnd()));

        return fullList;
    }

    @Override
    protected void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        // list to hold window of ALL incoming instructions;
        // list has size dictated by this.getConfig().getMaxsize()
        var instWindow = new SlidingWindow<Instruction>(this.getConfig().getMaxsize());

        // desired number of basic blocks in Megablock
        // save first instruction after a jump
        // first and last instruction in window must be same!
        // int blockNr = this.getConfig().getMaxBlocks() + 1;

        // process entire stream
        StreamUnit candidate = null;
        while ((candidate = getCandidate(istream, instWindow)) != null) {

            // turn pairs in to flat list
            var flatlist = this.expandTraceUnits(Arrays.asList(candidate), instWindow);

            // create new candidate hash sequence
            var newseq = super.hashSequence(flatlist);

            // add sequence to occurrence counters (counting varies between static to trace detection)
            super.addAddrToList(addrs, newseq);

            // add sequence to map which is indexed by hashCode + startaddr
            super.addHashSequenceToList(hashed, newseq);
        }

        // Remove all sequences which only happen once
        // BinarySegmentDetectionUtils.removeUnique(addrs, hashed);
    }
}

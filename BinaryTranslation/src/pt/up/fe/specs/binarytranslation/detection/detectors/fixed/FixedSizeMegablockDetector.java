package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.detectors.ASegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.BinarySegmentDetectionUtils;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.HashedSequence;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.MegaBlock;
import pt.up.fe.specs.binarytranslation.detection.segments.SegmentContext;
import pt.up.fe.specs.binarytranslation.detection.trace.TraceUnit;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * 
 * @author nuno
 *
 */
public class FixedSizeMegablockDetector extends ASegmentDetector {

    public FixedSizeMegablockDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * Check if candidate sequence is valid
     */
    private Boolean validSequence(List<TraceUnit> units) {

        // they all need to have different start addrs!
        for (int i = 0; i < units.size() - 1; i++) {
            for (int j = i + 1; j < units.size() - 1; j++) {
                var startAddr1 = units.get(i).getStart().getAddress().intValue();
                var startAddr2 = units.get(j).getStart().getAddress().intValue();
                if (startAddr1 == startAddr2)
                    return false;
            }
        }

        // except the first and last
        var finstStartAddr = units.get(0).getStart().getAddress().intValue();
        var linstStartAddr = units.get(units.size() - 1).getStart().getAddress().intValue();
        ;
        if (finstStartAddr != linstStartAddr)
            return false;
        else
            return true;
    }

    @Override
    protected BinarySegment makeSegment(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new MegaBlock(symbolicseq, contexts, this.getCurrentStream().getApp());
    }

    /*
     * Get the pair which represent the start and end of sequential block!
     */
    private TraceUnit getBlock(InstructionStream istream) {

        if (!istream.hasNext())
            return null;

        // find next jump
        Instruction startInst = istream.nextInstruction();
        Instruction previs = startInst, is = null;
        while ((is = istream.peekNext()) != null) {
            var addr1 = previs.getAddress().intValue();
            var addr2 = is.getAddress().intValue();

            // target of a taken branch
            if ((addr2 - addr1) != istream.getInstructionWidth())
                return new TraceUnit(startInst, previs);

            // advance
            previs = istream.nextInstruction();
        }

        return null;
    }

    // TODO: "advanceWindow" should go forward by as much as teh first TraceUnit in the trace unit window?

    @Override
    protected void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        // list to hold window of ALL incoming instructions;
        // list has size dictated by this.getConfig().getMaxsize()
        var auxInstList = new ArrayList<Instruction>();

        // list of trace Units (e.g., start-end addr pair of basic block)
        var window = new ArrayList<TraceUnit>();

        // desired number of basic blocks in Megablock
        // save first instruction after a jump
        // first and last instruction in window must be same!
        int blockNr = this.getConfig().getMaxBlocks() + 1;

        // get first set of @TraceUnit
        while (window.size() < blockNr && istream.hasNext())
            window.add(this.getBlock(istream));

        // process entire stream
        while (true) {

            // TODO: as I advance the window, if a new sequence of valid trace units is found
            // but the earliest of them has slid out the window, we need to discards that
            // sequence of TraceUnits

            // hash candidate
            if (validSequence(window)) {

                // turn pairs in to flat list
                var flatlist = new ArrayList<Instruction>();
                for (var pair : window.subList(0, window.size() - 1)) {
                    flatlist.add(pair.getStart());
                    flatlist.add(pair.getEnd());
                }

                // create new candidate hash sequence
                var newseq = BinarySegmentDetectionUtils.hashSequence(flatlist);

                // add sequence to occurrence counters (counting varies between static to trace detection)
                BinarySegmentDetectionUtils.addAddrToList(addrs, newseq);

                // add sequence to map which is indexed by hashCode + startaddr
                BinarySegmentDetectionUtils.addHashSequenceToList(hashed, newseq);
            }

            // find next block
            var block = this.getBlock(istream);
            if (block == null)
                break;

            window.remove(0);
            window.add(block);
        }
    }
}

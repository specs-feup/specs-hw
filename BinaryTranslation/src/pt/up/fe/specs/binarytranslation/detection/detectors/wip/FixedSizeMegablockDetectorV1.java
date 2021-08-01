package pt.up.fe.specs.binarytranslation.detection.detectors.wip;

import java.util.ArrayList;
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
import pt.up.fe.specs.binarytranslation.tracer.InstructionWindow;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnit;
import pt.up.fe.specs.binarytranslation.utils.SlidingWindow;

/**
 * 
 * @author nuno
 *
 */
public class FixedSizeMegablockDetectorV1 extends ASegmentDetector {

    public FixedSizeMegablockDetectorV1(DetectorConfiguration config) {
        super(config);
    }

    /*
     * Check if candidate sequence is valid
     */
    private Boolean validSequence(List<StreamUnit> units, SlidingWindow<Instruction> window) {

        // all start and end instruction of trace unit must exist in the window
        for (var unit : units)
            if (!window.exists(unit.getStart()) || !window.exists(unit.getEnd()))
                return false;

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
    private StreamUnit getTraceUnit(InstructionStream istream, InstructionWindow window) {

        if (!istream.hasNext())
            return null;

        // find next jump
        Instruction startInst = window.add(istream.nextInstruction());
        Instruction previs = startInst, is = null;
        while ((is = istream.peekNext()) != null) {
            var addr1 = previs.getAddress().intValue();
            var addr2 = is.getAddress().intValue();

            // target of a taken branch
            if ((addr2 - addr1) != istream.getInstructionWidth())
                return new StreamUnit(startInst, previs);

            // advance
            previs = window.add(istream.nextInstruction());
        }

        return null;
    }

    /*
     * Expands the TraceUnits into full instruction list
     */
    private List<Instruction> expandTraceUnits(List<StreamUnit> units, InstructionWindow window) {

        var fullList = new ArrayList<Instruction>();
        for (var unit : units)
            fullList.addAll(window.getRange(unit.getStart(), unit.getEnd()));

        return fullList;
    }

    /*
     * Checks the last instruction in the window, to see if it completes
     * a trace unit (?)
     
    private TraceUnit getTraceUnit(InstructionWindow window) {
        
        // last 2 insts in window
        var is1 = window.getFromLast(1);
        var is2 = window.getLast();
    
        // target of a taken branch
        if ((is2.getAddress().intValue() - is1.getAddress().intValue()) != istream.getInstructionWidth())
            return new TraceUnit(???, is1);        
    }*/

    // TODO: "advanceWindow" should go forward by as much as teh first TraceUnit in the trace unit window?

    @Override
    protected void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        // list to hold window of ALL incoming instructions;
        // list has size dictated by this.getConfig().getMaxsize()
        var instWindow = new SlidingWindow<Instruction>(this.getConfig().getMaxsize());

        // list of trace Units (e.g., start-end addr pair of basic block)
        var unitWindow = new ArrayList<StreamUnit>();

        // desired number of basic blocks in Megablock
        // save first instruction after a jump
        // first and last instruction in window must be same!
        int blockNr = this.getConfig().getMaxBlocks() + 1;

        // get first set of @TraceUnit
        while (unitWindow.size() < blockNr && istream.hasNext())
            unitWindow.add(this.getTraceUnit(istream, instWindow));

        // process entire stream
        while (true) {

            // hash candidate
            if (validSequence(unitWindow, instWindow)) {

                // sort trace units by lowest starting addr? (obeys megablock definition?)
                var sortUnits = unitWindow.subList(0, blockNr - 1);
                // Collections.sort(sortUnits,
                // (o1, o2) -> o1.getStart().getAddress().compareTo(o2.getStart().getAddress()));

                // turn pairs in to flat list
                var flatlist = this.expandTraceUnits(sortUnits, instWindow);

                // create new candidate hash sequence
                var newseq = super.hashSequence(flatlist);

                // add sequence to occurrence counters (counting varies between static to trace detection)
                super.addAddrToList(addrs, newseq);

                // add sequence to map which is indexed by hashCode + startaddr
                super.addHashSequenceToList(hashed, newseq);
            }

            // find next block
            var block = this.getTraceUnit(istream, instWindow);
            if (block == null)
                break;

            unitWindow.remove(0);
            unitWindow.add(block);
        }

        // Remove all sequences which only happen once
        // BinarySegmentDetectionUtils.removeUnique(addrs, hashed);
    }
}

package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import java.util.List;

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
                .getCpuArchitectureName()
                .getResource().equals("microblaze32")) // NOTE: this is a quick hack

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
        var targetAddr = last.getBranchTarget().longValue(); // TODO what if branch is based on register values?
        var firstAddr = window.get(0).getAddress().longValue();
        if (targetAddr != firstAddr)
            return false;

        return true;
    }
}

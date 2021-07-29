package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.detectors.ASegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.HashedSequence;
import pt.up.fe.specs.binarytranslation.detection.detectors.v3.SlidingWindow;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public abstract class ASimpleSegmentDetector extends ASegmentDetector {

    private List<Instruction> processedInsts = new ArrayList<>();

    /*
     * 
     */
    public ASimpleSegmentDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * Check if candidate sequence is valid
     */
    protected Boolean validSequence(SlidingWindow<Instruction> window) {

        // start and end addrs
        var sAddr = window.get(0).getAddress();
        if (sAddr < this.getConfig().getStartAddr().longValue()
                || sAddr > this.getConfig().getStopAddr().longValue())
            return false;

        var eAddr = window.getLast().getAddress();
        if (eAddr < this.getConfig().getStartAddr().longValue()
                || eAddr > this.getConfig().getStopAddr().longValue())
            return false;

        // cant end on an incomplete "atomic" instruction
        if (window.getLast().getDelay() > 0)
            return false;

        // TEMPORARY HACK: cant have only LOAD or STORE (since this is usually stack operations)
        // and including them skews the detection results
        for (var inst : window.getWindow()) {
            if (!inst.isLoad() && !inst.isStore())
                return true;
        }
        return false;

        // return true;
    }

    @Override
    public void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        var window = new SlidingWindow<Instruction>(this.getConfig().getMaxsize());

        if (this.getConfig().getSkipToAddr().longValue() != -1)
            istream.advanceTo(this.getConfig().getSkipToAddr().longValue());

        // MODIFICATION FOR IEEE MICRO DATA GATHERING
        istream.setCycleCounterBounds(
                this.getConfig().getStartAddr(),
                this.getConfig().getStopAddr());

        // make 1st window
        while (!window.isFull()) {
            var inst = istream.nextInstruction();
            // this.processedInsts.add(inst);
            window.add(inst);
        }

        // process entire stream
        while (true) {

            // discard candidate?
            if (validSequence(window)) {

                // create new candidate hash sequence
                var newseq = super.hashSequence(window.getWindow());

                // add sequence to occurrence counters (counting varies between static to trace detection)
                super.addAddrToList(addrs, newseq);

                // add sequence to map which is indexed by hashCode + startaddr
                super.addHashSequenceToList(hashed, newseq);
            }

            // done
            if (!istream.hasNext())
                break;

            // shift window by 1
            else {
                var inst = istream.nextInstruction();
                // this.processedInsts.add(inst);
                window.add(inst);
            }

            // check for premature quit condition
            if (window.getLast().getAddress().longValue() == this.getConfig().getPrematureStopAddr().longValue())
                break;
        }

        // Remove all sequences which only happen once
        super.removeUnique(addrs, hashed);
    }
}

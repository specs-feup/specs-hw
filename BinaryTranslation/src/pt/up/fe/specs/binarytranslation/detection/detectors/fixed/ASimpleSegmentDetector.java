package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.detectors.ASegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.HashedSequence;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.tracer.InstructionWindow;

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
    protected Boolean validSequence(InstructionWindow window) {

        // FIXME: Sometimes window.getLast() is null
        // if (window.getLast() == null) {
        // return false;
        // }

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

        return true;
    }

    @Override
    public void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        var window = new InstructionWindow(this.getConfig().getMaxsize());

        // make 1st window
        while (!window.isFull()) {
            var inst = istream.nextInstruction();
            this.processedInsts.add(inst);
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
                this.processedInsts.add(inst);
                window.add(inst);
            }

            // check for premature quit condition
            if (window.getLast().getAddress() == this.getConfig().getPrematureStopAddr())
                break;
        }

        // Remove all sequences which only happen once
        super.removeUnique(addrs, hashed);
    }

    public List<Instruction> getProcessedInsts() {
        return processedInsts;
    }
}

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
     
    private Boolean validSequence(List<Instruction> insts) {
    
        // check if all branches except the last are forward
        for (int i = 0; i < insts.size() - 1; i++) {
            if (insts.get(i).isBackwardsJump())
                return false;
        }
    
        // check last one
        if (!insts.get(insts.size() - 1).isBackwardsJump())
            return false;
    
        return true;
    }*/

    /*
     * Check if candidate sequence is valid
     */
    private Boolean validSequence(List<Instruction> insts) {

        // they all need to be different!
        for (int i = 0; i < insts.size() - 1; i++) {
            for (int j = i + 1; j < insts.size() - 1; j++) {
                var is1 = insts.get(i);
                var is2 = insts.get(j);
                if (is1.getAddress().intValue() == is2.getAddress().intValue())
                    return false;
            }
        }

        // except the first and last
        var finst = insts.get(0);
        var linst = insts.get(insts.size() - 1);
        if (finst.getAddress().intValue() != linst.getAddress().intValue())
            return false;
        else
            return true;
    }

    @Override
    protected BinarySegment makeSegment(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new MegaBlock(symbolicseq, contexts, this.getCurrentStream().getApp());
    }

    private Instruction getNextTarget(InstructionStream istream) {

        // find next jump
        int savectr = 0;
        Instruction is = null;
        while ((is = istream.nextInstruction()) != null) {
            if (is.isJump()) {
                savectr = is.getDelay() + 1;
            }
        }

        // get target
        while (savectr-- != 0 && (is = istream.nextInstruction()) != null)
            ;
        return is;
    }

    @Override
    protected void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        // list of branch target addrs, can be used to reconstruct block
        var window = new ArrayList<Instruction>();

        // desired number of basic blocks in Megablock
        // save first instruction after a jump
        // first and last instruction in window must be same!
        int blockNr = this.getConfig().getMaxBlocks() + 1;

        // get first window
        while (window.size() < blockNr && istream.hasNext()) {
            window.add(this.getNextTarget(istream));

            /*var is = istream.nextInstruction();
            
            if (savectr > 0) {
                savectr--;
                if (savectr == 0) {
                    window.add(is);
                }
            }
            
            if (is.isJump()) {
                savectr = is.getDelay() + 1;
            }*/
        }

        // process entire stream
        do {

            // hash candidate
            if (validSequence(window)) {

                // create new candidate hash sequence
                var newseq = BinarySegmentDetectionUtils.hashSequence(window);

                // add sequence to occurrence counters (counting varies between static to trace detection)
                BinarySegmentDetectionUtils.addAddrToList(addrs, newseq);

                // add sequence to map which is indexed by hashCode + startaddr
                BinarySegmentDetectionUtils.addHashSequenceToList(hashed, newseq);
            }

            // find next branch
            window.remove(0);
            window.add(this.getNextTarget(istream));

            // TODO handle the possibily of nextInstruction being nulls in the middle of the loop...

        } while (istream.hasNext());
    }
}

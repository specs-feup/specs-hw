package pt.up.fe.specs.binarytranslation.tracer;

import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.detection.detectors.v3.SlidingWindow;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

/**
 * Reduces incoming instructions into different types of trace units In the simplest case, the StreamTracer is just a
 * passthrough for instructions, packed into the @StreamInstruction object
 */
public class StreamUnitGenerator {

    private TraceInstructionStream istream;
    private SlidingWindow<Instruction> window; // useless??

    public StreamUnitGenerator(TraceInstructionStream istream) {
        this.istream = istream;
        this.window = new SlidingWindow<Instruction>(100); // TODO edit this default
    }

    /*
     * 
     */
    public void advanceTo(long addr) {
        this.istream.advanceTo(addr);
    }

    /*
     * 
     */
    public StreamInstruction nextInstruction() {
        var inst = istream.nextInstruction();
        if (inst == null)
            return null;

        else {
            this.window.add(inst); // keep for whatever?
            return new StreamInstruction(inst);
        }
    }

    /*
     * 
     */
    public StreamBasicBlock nextBasicBlock() {

        // get TraceUnits until a branch happens
        var bbtype = StreamUnitType.StreamBasicBlock_fwd;
        var delayctr = 0;
        var tilist = new ArrayList<StreamInstruction>();
        while (true) {
            var ti = this.nextInstruction();
            if (ti == null)
                return null; // ugly but works for now...

            tilist.add(ti);
            if (ti.getActual().isJump()) {
                delayctr = ti.getActual().getDelay();
                bbtype = ti.getActual().isBackwardsJump()
                        ? StreamUnitType.StreamBasicBlock_back
                        : StreamUnitType.StreamBasicBlock_fwd;
                break;
            }
        }

        // consume delay slot
        while (delayctr-- > 0)
            tilist.add(this.nextInstruction());

        return new StreamBasicBlock(tilist, bbtype);
    }

    /*
     * 
     */
    public StreamSuperBlock nextSuperBlock() {

        var tbblist = new ArrayList<StreamBasicBlock>();
        while (true) {
            var nbb = this.nextBasicBlock();
            if (nbb == null)// || nbb.getType() == TraceUnitType.TraceBasicBlock_back)
                break;

            // add
            tbblist.add(nbb);

            // if backwards, end block, but add
            if (nbb.getType() == StreamUnitType.StreamBasicBlock_back)
                break;
        }

        // ugly but works for now
        if (tbblist.size() > 0)
            return new StreamSuperBlock(tbblist);
        else
            return null;
    }

    /*
     * 
     */
    public boolean hasNext() {
        return this.istream.hasNext();
    }
}

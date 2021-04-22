package pt.up.fe.specs.binarytranslation.tracer;

import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * Reduces incoming instructions into different types of trace units In the simplest cast, the StreamTracer is just a
 * passthrough for instructions, packed into the TraceInstruction object
 */
public class StreamTracer {

    private InstructionStream istream;
    private InstructionWindow window;

    // TODO need buffer for previous traceunit and then append/discard oldest??
    // e.g. list of basic blocks etc

    public StreamTracer(InstructionStream istream) {
        this.istream = istream;
        this.window = new InstructionWindow(100); // TODO edit this default
    }

    /*
     * 
     */
    public TraceInstruction nextInstruction() {
        var inst = istream.nextInstruction();
        if (inst == null)
            return null;

        else {
            this.window.add(inst); // keep for whatever?
            return new TraceInstruction(inst);
        }
    }

    /*
     * 
     */
    public TraceBasicBlock nextBasicBlock() {

        // get TraceUnits until a branch happens
        var bbtype = TraceUnitType.TraceBasicBlock_fwd;
        var delayctr = 0;
        var tilist = new ArrayList<TraceInstruction>();
        while (true) {
            var ti = this.nextInstruction(); // TODO: one instruction is throw away here for the next call... is it?...
            if (ti == null)
                return null; // ugly but works for now...

            tilist.add(ti);
            if (ti.getActual().isJump()) {
                delayctr = ti.getActual().getDelay();
                bbtype = ti.getActual().isBackwardsJump()
                        ? TraceUnitType.TraceBasicBlock_back
                        : TraceUnitType.TraceBasicBlock_fwd;
                break;
            }
        }

        // consume delay slot
        while (delayctr-- > 0)
            tilist.add(this.nextInstruction());

        return new TraceBasicBlock(tilist, bbtype);
    }

    /*
     * 
     */
    public TraceSuperBlock nextSuperBlock(int maxsize) {

        var tbblist = new ArrayList<TraceBasicBlock>();
        while (maxsize-- > 0) {
            var nbb = this.nextBasicBlock();
            if (nbb == null || nbb.getType() == TraceUnitType.TraceBasicBlock_back)
                break;

            // add if forwards branch
            tbblist.add(nbb);
        }

        // ugly but works for now
        if (tbblist.size() > 0)
            return new TraceSuperBlock(tbblist);
        else
            return null;
    }

    // TODO: the tracer should output TraceUnits of a requested type
}

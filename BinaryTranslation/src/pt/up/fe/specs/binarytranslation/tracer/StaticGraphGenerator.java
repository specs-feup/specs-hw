package pt.up.fe.specs.binarytranslation.tracer;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class StaticGraphGenerator {

    private StreamTracer tracer;

    public StaticGraphGenerator(InstructionStream istream) {
        this.tracer = new StreamTracer(istream);
    }

    /*
     * 
     */
    private TraceUnit skipTo(Number startAddr) {

        // advance until start of kernel
        TraceUnit next = null;
        do {
            next = tracer.nextBasicBlock();
        } while (next.getStart().getAddress().longValue() != startAddr.longValue());

        return next;
    }

    /*
     * 
     */
    public TraceUnitGraphT generateStaticGraph() {
        return this.generateStaticGraph(0x00000000, 0xFFFFFFFF);
    }

    /*
     * 
     */
    public TraceUnitGraphT generateStaticGraph(Number startAddr) {
        return this.generateStaticGraph(startAddr, 0xFFFFFFFF);
    }

    /*
     * 
     */
    public TraceUnitGraphT generateStaticGraph(Number startAddr, Number stopAddr) {

        // basic blocks
        var tgraph = new TraceUnitGraphT();

        // skip to start
        var first = this.skipTo(startAddr);
        tgraph.insert(first);

        // int i = 200; // just for testing!
        while (tracer.hasNext()) {

            // next block
            var next = tracer.nextBasicBlock();
            tgraph.insert(next);
            if (next.getEnd().getAddress().longValue() == stopAddr.longValue())
                break;

            // count down
            /*i--;
            if (i == 0)
                break;*/
        }

        return tgraph;
    }
}

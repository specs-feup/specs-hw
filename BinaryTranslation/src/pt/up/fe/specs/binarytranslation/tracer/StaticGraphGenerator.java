package pt.up.fe.specs.binarytranslation.tracer;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class StaticGraphGenerator {

    public static TraceUnitGraph generateStaticGraph(InstructionStream istream) {

        // basic blocks
        var tracer = new StreamTracer(istream);

        // head
        var head = new TraceGraphNode(tracer.nextBasicBlock());
        var tgraph = new TraceUnitGraph(head);

        int i = 50; // just for testing!
        while (tracer.hasNext()) {

            // next block
            var next = tracer.nextBasicBlock();
            System.out.println(next);
            tgraph.insert(new TraceGraphNode(next));

            // count down
            i--;
            if (i == 0)
                break;
        }

        return tgraph;
    }
}

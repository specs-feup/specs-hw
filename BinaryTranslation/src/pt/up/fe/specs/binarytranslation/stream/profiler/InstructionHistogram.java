package pt.up.fe.specs.binarytranslation.stream.profiler;

import java.util.HashMap;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class InstructionHistogram extends AInstructionStreamProfiler {

    private HashMap<String, Integer> histogram;

    public InstructionHistogram(InstructionStream istream) {
        super(istream);
        this.histogram = new HashMap<String, Integer>();
    }

    @Override
    public void profile() {
        while (this.istream.hasNext()) {
            var inst = this.istream.nextInstruction();
            if (inst == null)
                break;

            if (this.histogram.containsKey(inst.getName())) {
                var count = this.histogram.get(inst.getName());
                this.histogram.put(inst.getName(), count + 1);
            } else {
                this.histogram.put(inst.getName(), 1);
            }
        }
    }

    public HashMap<String, Integer> getHistogram() {
        return this.histogram;
    }
}

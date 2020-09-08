package pt.up.fe.specs.binarytranslation.stream.profiler;

import java.util.function.Function;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public abstract class AHistogramProfile extends AInstructionStreamProfiler {

    private Function<Instruction, String> getKey;

    public AHistogramProfile(InstructionStream istream, Function<Instruction, String> getKey) {
        super(istream);
        this.getKey = getKey;
    }

    @Override
    public InstructionProfileResult profile() {
        ProfileHistogram histogram = new ProfileHistogram();
        var hist = histogram.getHistogram();

        while (this.istream.hasNext()) {

            var inst = this.istream.nextInstruction();
            if (inst == null)
                break;

            var key = this.getKey.apply(inst);
            if (hist.containsKey(key)) {
                var count = hist.get(key);
                hist.put(key, count + 1);

            } else {
                hist.put(key, 1);
            }
        }

        return histogram;
    }
}

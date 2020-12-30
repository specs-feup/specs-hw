package pt.up.fe.specs.binarytranslation.profiling;

import pt.up.fe.specs.binarytranslation.instruction.InstructionType;

public class InstructionTypeHistogram extends AHistogramProfile {

    public InstructionTypeHistogram() {
        super(data -> {
            var type = data.getData().getGenericTypes().get(0).toString();

            // fix for arm
            if (type.equals(InstructionType.G_MEMORY.toString())) {
                type = data.getData().getGenericTypes().get(1).toString();
            }
            return type;
        });
    }
}

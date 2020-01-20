package pt.up.fe.specs.binarytranslation.graphs;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;

/**
 * This class implements the conversion of instructions to graph node types The creation of a graph node type should be
 * based on the {@link InstructionType} value
 * 
 * @author nuno
 *
 */
public class GraphNodeCatalogue {

    public static GraphOperationType convertInstruction(Instruction i) {

        GraphOperationType gtype = null;
        var types = i.getData().getGenericTypes();

        // addition
        if (types.contains(InstructionType.G_ADD)) {
            gtype = GraphOperationType.add;
        }

        // TODO: add others
        // figure out how to process subtypes and side effects, and multiple inputs/outputs

        // unknown type
        else {
            gtype = GraphOperationType.unknown;
        }

        return gtype;
    }
}

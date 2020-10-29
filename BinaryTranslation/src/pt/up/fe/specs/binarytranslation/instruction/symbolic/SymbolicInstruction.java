package pt.up.fe.specs.binarytranslation.instruction.symbolic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * A special type of instruction which contains only string type operands, and can be converted to a concrete
 * instruction when a context is applied; the context should include operand values and inst addr
 * 
 * @author nuno
 *
 */
public class SymbolicInstruction {

    private String mmemonic;
    private List<String> operandSymbol = new ArrayList<String>();

    // TODO: needs list of Operand type objects so I can query sizes and stuf??
    // or should this Instruction NOT implement Instruction interface??

    public SymbolicInstruction(Instruction i, Map<String, String> regremap) {

        this.mmemonic = i.getName();
        for (var op : i.getData().getOperands()) {

            try {
                var rep = op.getRepresentation();
                if (!regremap.containsKey(rep))
                    throw new Exception();

                this.operandSymbol.add(regremap.get(rep));

                // TODO: handle beter
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public String getRepresentation() {
        var bld = new StringBuilder();
        bld.append(this.mmemonic);
        for (var op : this.operandSymbol)
            bld.append(" ").append(op);

        return bld.toString();
    }
}

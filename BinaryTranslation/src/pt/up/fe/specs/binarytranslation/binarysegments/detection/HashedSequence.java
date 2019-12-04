package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.SimpleInstruction;

public class HashedSequence {

    private Number addr;
    private Map<String, String> regremap;
    private List<SimpleInstruction> instlist;

    HashedSequence(List<Instruction> instlist, Number addr, Map<String, String> regremap) {

        // deep copy
        this.instlist = new ArrayList<SimpleInstruction>();
        for (Instruction i : instlist)
            this.instlist.add(new SimpleInstruction(i));

        this.addr = addr;
        this.regremap = regremap;
    }

    public List<SimpleInstruction> getInstlist() {
        return instlist;
    }

    public Map<String, String> getRegremap() {
        return regremap;
    }

    public Number getAddr() {
        return addr;
    }
}

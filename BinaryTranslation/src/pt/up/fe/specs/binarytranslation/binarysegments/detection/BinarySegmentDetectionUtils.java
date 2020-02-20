package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.instruction.OperandType;

public class BinarySegmentDetectionUtils {

    /*
     * NOTE: most of these methods operate under the assumption that the "hashed" map is configured as such:
     * <hashcode_startaddr, HashedSequence>
     * Where "hashcode_startaddr" is built by concatenating the string 
     * representations of the the hashcode of the sequence and its starting address
     */

    /*
     * Remove all sequences which only happen once
     */
    protected static void removeUnique(Map<Integer, List<Integer>> addrs, Map<String, HashedSequence> hashed) {

        // iterate through hashcodes of sequences
        Iterator<Integer> it = addrs.keySet().iterator();

        while (it.hasNext()) {
            var hashcode = it.next();
            var addrlist = addrs.get(hashcode);
            if (addrlist.size() <= 1) {

                // remove hashed sequence from hashed sequences list by its starting addr
                var keyval = hashcode.toString() + "_" + Integer.toString(addrlist.get(0));
                hashed.remove(keyval);
                it.remove();
            }
        }
    }

    /*
     * Adds address of detected sequence to auxiliary list "this.addrs"
     */
    protected static void addAddrToList(Map<Integer, List<Integer>> addrs, HashedSequence newseq) {

        var hashCode = newseq.getHashcode();
        var startAddr = newseq.getStartAddress();

        // add sequence addr to list, if equivalent already exists
        if (addrs.containsKey(hashCode)) {
            if (!addrs.get(hashCode).contains(startAddr))
                addrs.get(hashCode).add(startAddr);
        }

        // add hashcode to addr list map
        else {
            var l = new ArrayList<Integer>();
            l.add(startAddr);
            addrs.put(hashCode, l);
        }
    }

    /*
     * Adds detected sequence to list "this.hashed"
     */
    protected static void addHashSequenceToList(Map<String, HashedSequence> hashed, HashedSequence newseq) {

        var hashCode = newseq.getHashcode();
        var startAddr = newseq.getStartAddress();

        // add sequence to map which is indexed by hashCode + startaddr
        var keyval = Integer.toString(hashCode)
                + "_" + Integer.toString(startAddr);

        if (!hashed.containsKey(keyval)) {
            hashed.put(keyval, newseq);

            // Info prints
            System.out.println("Found Basic Block at 0x" + newseq.getStartAddress());
        }

        // useful for traces
        else {
            hashed.get(keyval).incrementOccurences();

            // Info prints
            System.out.println("\tincremented count of basic block "
                    + keyval + " to " + hashed.get(keyval).getOcurrences());
        }
    }

    /*
     * 
     */
    protected static HashedSequence hashSequence(List<Instruction> candidate) {

        // make register replacement map (for hash building)
        Map<String, String> regremap = BinarySegmentDetectionUtils.makeRegReplaceMap(candidate);

        String hashstring = "";
        for (Instruction inst : candidate) {

            // make part 1 of hash string
            hashstring += "_" + Integer.toHexString(inst.getProperties().getOpCode());
            // TODO this unique id (the opcode) will not be unique for arm, since the
            // specific instruction is resolved later with fields that arent being
            // interpreted yet; how to solve?
            // TODO replace getOpCode with getUniqueID() (as a string)

            // make part 2 of hash string
            for (Operand op : inst.getData().getOperands()) {
                hashstring += "_" + regremap.get(op.getRepresentation());
                // at this point, imms have either been (or not) all (or partially) symbolified
            }
        }

        // return hashed sequence
        Integer hashCode = hashstring.hashCode();
        return new HashedSequence(hashCode, candidate, regremap);
    }

    /*
     * Builds operand value replacement map for a given sequence (assumed valid)
     */
    protected static Map<String, String> makeRegReplaceMap(List<Instruction> ilist) {

        Map<OperandType, Character> counter = new HashMap<OperandType, Character>();
        Map<String, String> regremap = new HashMap<String, String>();
        for (Instruction i : ilist) {

            var operands = i.getData().getOperands();
            for (Operand op : operands) {

                // register must not be special (e.g. stack pointer in ARM)
                if (op.isSpecial())
                    continue;

                if (!regremap.containsKey(op.getRepresentation())) {

                    // get current count
                    char c;

                    // OperandType
                    var typeid = op.getProperties().getMainType();

                    // TODO should be an exception here if operand is symbolic
                    // must be non symbolic REGISTER or IMMEDIATE

                    if (!counter.containsKey(typeid))
                        counter.put(typeid, 'a');
                    else {
                        c = counter.get(typeid).charValue();
                        counter.put(typeid, Character.valueOf(++c));
                    }

                    // remap
                    c = counter.get(typeid).charValue();
                    regremap.put(op.getRepresentation(), op.getPossibleSymbolicRepresentation(String.valueOf(c)));
                }
            }

            // TODO implement imm remapping strategies here??
        }

        return regremap;
    }
}

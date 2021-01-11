package pt.up.fe.specs.binarytranslation.detection.detectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandType;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.InstructionStreamType;

public abstract class ASegmentDetector implements SegmentDetector {

    private InstructionStream currentStream = null;
    private DetectorConfiguration config;

    /*
     * Stuff for statistics (TODO: add more) 
     */
    // protected long totalCycles;
    // protected long numInsts;
    // protected int totalMemoryInsts;
    // protected int totalLoads;
    // protected int totalStores;
    // protected int totalBranchInsts;
    // protected int totalTakenBranches;
    // protected int totalNonTakenBranches;

    public ASegmentDetector(DetectorConfiguration config) {
        this.config = config;
    }

    public DetectorConfiguration getConfig() {
        return config;
    }

    protected void setCurrentStream(InstructionStream currentStream) {
        this.currentStream = currentStream;
    }

    protected InstructionStream getCurrentStream() {
        return currentStream;
    }

    /*
     * Remove all sequences which only happen once
     */
    protected void removeUnique(Map<Integer, List<Integer>> addrs, Map<String, HashedSequence> hashed) {

        // addrs
        // This map holds all hashed sequences for all instruction windows we analyze
        // Map: <hashcode_startaddr, hashedsequence>

        // hashed
        // This map holds all hash codes and list of occurring addresses for each
        // Map: <hashcode, list of addresses>

        // iterate through hashcodes of sequences
        var it = addrs.keySet().iterator();

        while (it.hasNext()) {
            var hashcode = it.next();
            var addrlist = addrs.get(hashcode);

            // QUICK HACK!!! TODO: fix this mess

            // if static
            if (this.getCurrentStream().getType() == InstructionStreamType.STATIC_ELF) {
                if (addrlist.size() <= 1) {

                    // remove hashed sequence from hashed sequences list by its starting addr
                    var keyval = hashcode.toString() + "_" + Integer.toString(addrlist.get(0));
                    hashed.remove(keyval);
                    it.remove();
                }
            }

            // InstructionStreamType.TRACE
            else {

                var addrit = addrlist.iterator();
                while (addrit.hasNext()) {
                    var curraddr = addrit.next();

                    // if trace sequence happens once, remove from hashed map
                    var keyval = hashcode.toString() + "_" + Integer.toString(curraddr);
                    var hashedseq = hashed.get(keyval);

                    // and remove its addr from the list of sequences with the same hash
                    // but different addresses
                    if (hashedseq.getOcurrences() == 1) {
                        hashed.remove(keyval);
                        addrit.remove();
                    }
                }

                // remove list of addresses from map of address lists if empty
                if (addrlist.size() == 0)
                    it.remove();
            }
        }
    }

    /*
     * Adds address of detected sequence to auxiliary list "this.addrs"
     */
    protected void addAddrToList(Map<Integer, List<Integer>> addrs, HashedSequence newseq) {

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
     * Builds operand value replacement map for a given sequence (assumed valid)
     */
    protected Map<String, String> makeRegReplaceMap(List<Instruction> ilist) {

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
                    var typeid = op.getProperties().getType();

                    // TODO should be an exception here if operand is symbolic
                    // must be non symbolic REGISTER or IMMEDIATE
                    if (typeid != OperandType.REGISTER && typeid != OperandType.IMMEDIATE)
                        continue;

                    if (!counter.containsKey(typeid))
                        counter.put(typeid, 'a');
                    else {
                        c = counter.get(typeid).charValue();
                        counter.put(typeid, Character.valueOf(++c));
                    }

                    // remap
                    c = counter.get(typeid).charValue();
                    var newrep = op.getProperties().getPrefix() + "<" + String.valueOf(c) + ">";
                    regremap.put(op.getRepresentation(), newrep);
                }
            }

            // TODO implement imm remapping strategies here??
        }

        return regremap;
    }

    /*
     * Adds detected sequence to list "this.hashed"
     */
    protected void addHashSequenceToList(Map<String, HashedSequence> hashed, HashedSequence newseq) {

        var hashCode = newseq.getHashcode();
        var startAddr = newseq.getStartAddress();

        // add sequence to map which is indexed by hashCode + startaddr
        var keyval = Integer.toString(hashCode)
                + "_" + Integer.toString(startAddr);

        if (!hashed.containsKey(keyval)) {
            hashed.put(keyval, newseq);

            // Info prints
            // System.out.println("Found Basic Block at 0x" + newseq.getStartAddress());
        }

        // useful for traces
        else {
            hashed.get(keyval).incrementOccurences();

            // Info prints
            // System.out.println("\tincremented count of basic block "
            // + keyval + " to " + hashed.get(keyval).getOcurrences());
        }
    }

    /*
     * TODO: define the hash function as an option of detection
     * since i might want to hash things based only on the opcode, or addr 
     */
    protected HashedSequence hashSequence(List<Instruction> candidate) {

        // make register replacement map (for hash building)
        Map<String, String> regremap = this.makeRegReplaceMap(candidate);

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
     * Must be implemented by children
     */
    protected abstract BinarySegment makeSegment(List<Instruction> symbolicseq, List<SegmentContext> contexts);

    /*
     * For all valid hashcodes, make the symbolic sequence and its in/out contexts
     */
    protected List<BinarySegment> makeSegments(Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        /*
         * The list this detector will construct
         */
        List<BinarySegment> allsequences = new ArrayList<BinarySegment>();

        // all start addrs grouped by hashcode
        Iterator<Integer> it = addrs.keySet().iterator();

        // for each hashcode
        while (it.hasNext()) {

            // get hashcode
            var hashcode = it.next();

            // get all start addrs of all sequences with this hashcode
            var addrlist = addrs.get(hashcode);

            // get a list of the sequences by their hashcode_startaddr key
            var seqlist = new ArrayList<HashedSequence>();
            for (Integer startaddr : addrlist) {
                var keyval = hashcode.toString() + "_" + Integer.toString(startaddr);
                seqlist.add(hashed.get(keyval));
            }
            // TODO: fix, this is only rebuilding a haash i built before ( where?! ) , and i prone to be wrong and is
            // messy...

            // use first sequence with this hash code to create symbolic sequence
            var symbolicseq = seqlist.get(0).makeSymbolic();

            // Create all contexts
            var contexts = new ArrayList<SegmentContext>();
            for (HashedSequence seq : seqlist)
                contexts.add(new SegmentContext(seq));

            // make the frequent sequence
            var newseq = this.makeSegment(symbolicseq, contexts);
            allsequences.add(newseq);
        }

        return allsequences;
    }

    /*
     * Must be implemented by children
     */
    protected abstract void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs);

    @Override
    public SegmentBundle detectSegments(InstructionStream istream) {

        this.setCurrentStream(istream);

        // This map holds all hashed sequences for all instruction windows we analyze
        // Map: <hashcode_startaddr, hashedsequence>
        Map<String, HashedSequence> hashed = new HashMap<String, HashedSequence>();

        // This map holds all hash codes and list of occurring addresses for each
        // Map: <hashcode, list of addresses>
        Map<Integer, List<Integer>> addrs = new HashMap<Integer, List<Integer>>();

        // process entire stream
        this.processStream(istream, hashed, addrs);

        // for all valid hashed sequences, make the StaticBasicBlock objects
        var segments = this.makeSegments(hashed, addrs);

        // finally, init some stats
        SegmentBundle bundle = new SegmentBundle(segments, istream);

        //
        this.setCurrentStream(null);

        return bundle;
    }
}

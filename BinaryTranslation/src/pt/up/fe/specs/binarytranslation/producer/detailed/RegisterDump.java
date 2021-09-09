package pt.up.fe.specs.binarytranslation.producer.detailed;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.LineStream;

public class RegisterDump {

    // TODO: replace "String" by AsmFieldName!

    private Map<String, Long> regs;
    // public static RegisterDump nullDump = new RegisterDumpNull();

    public RegisterDump() {
        this.regs = new HashMap<>();
    }

    /*
     * Deep copy TODO: does this deep copy??
     */
    public RegisterDump(RegisterDump that) {
        this();
        for (var key : that.regs.keySet()) {
            this.regs.put(key, that.regs.get(key));
        }
    }

    // TODO pass thing function to GDB method getRegisters?
    public static RegisterDump newInstance(String rawRegisterDump, Pattern matchpat) {

        var dump = new RegisterDump();
        var lstream = LineStream.newInstance(rawRegisterDump);
        while (lstream.peekNextLine() != null) {
            var line = lstream.nextLine();
            if (!SpecsStrings.matches(line, matchpat))
                continue;

            var regAndValue = SpecsStrings.getRegex(line, matchpat);
            var reg = regAndValue.get(0).trim();
            var value = Long.valueOf(regAndValue.get(1).trim(), 16);
            dump.regs.put(reg, value);
        }
        return dump;
    }

    public void add(String register, long value) {
        regs.put(register, value);
    }

    public void add(RegisterDump mergeRegs) {
        for (var m : mergeRegs.getRegisterMap().keySet()) {
            regs.put(m, mergeRegs.getValue(m));
        }
    }

    public Long getValue(String register) {
        return regs.get(register);
    }

    @Override
    public String toString() {
        var sBuilder = new StringBuilder();

        var sortedMap = new TreeMap<String, Long>();
        sortedMap.putAll(this.regs);
        int mod = sortedMap.keySet().size() / 3;

        int i = 1;
        for (var key : sortedMap.keySet()) {
            sBuilder.append(key + ": 0x" + Long.toHexString(sortedMap.get(key)));
            sBuilder.append(i % mod == 0 ? "\n" : ",\t");
            i++;
        }

        return sBuilder.toString();
    }

    public void prettyPrint() {
        System.out.println(this.toString());
    }

    public boolean isEmpty() {
        return regs.size() == 0;
    }

    public Map<String, Long> getRegisterMap() {
        return regs;
    }
}

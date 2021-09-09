package pt.up.fe.specs.binarytranslation.instruction.register;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmField;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.LineStream;

public class ARegisterDump implements RegisterDump {

    // private final Map<String, AsmField> regnames;
    private final Map<AsmField, Number> regvalues;

    public ARegisterDump(Map<String, AsmField> regnames, Map<AsmField, Number> regvalues) {
        this.regnames = regnames;
        this.regvalues = regvalues;
    }

    /*
     * Deep copy TODO: does this deep copy??
     
    public ARegisterDump(ARegisterDump that) {
        for (var key : that.regs.keySet())
            this.regs.put(key, that.regs.get(key));
    }*/

    // TODO: pass a rawRegisterDump here, together with a HashMap
    // that links fieldnames to register string names

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

    @Override
    public Number getValue(AsmField registerName) {
        return regs.get(registerName);
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

    @Override
    public void prettyPrint() {
        System.out.println(this.toString());
    }

    @Override
    public boolean isEmpty() {
        return regvalues.size() == 0;
    }

    @Override
    public Map<AsmField, Number> getRegisterMap() {
        return regvalues;
    }

}

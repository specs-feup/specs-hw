package pt.up.fe.specs.binarytranslation.instruction.register;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.LineStream;

public abstract class ARegisterDump implements RegisterDump {

    // map of register definitions to values
    private final Map<Register, Number> regvalues;

    // helps look up by register string name
    private final Map<String, Register> helperMap;

    protected ARegisterDump(Map<Register, Number> regvalues) {
        this.regvalues = regvalues;
        this.helperMap = new HashMap<String, Register>();
        for (var v : this.regvalues.keySet()) {
            helperMap.put(v.getName(), v);
        }
    }

    /*
     * copy
     */
    protected ARegisterDump(ARegisterDump that) {
        this.regvalues = that.regvalues;
        this.helperMap = that.helperMap;
    }

    /*
     * (Must be implemented by children to preserver copied object type)
     */
    @Override
    public abstract RegisterDump copy();

    /*
     * 
     */
    private static final Pattern REGPATTERN = Pattern.compile("([0-9a-z]+)\\s*0x([0-9a-f]+)\\s.*");

    protected static Map<Register, Number> parseRegisters(List<Register> registerDefinitions, String rawRegisterDump) {

        // helps look up
        var helperMap = new HashMap<String, Register>();
        for (var v : registerDefinitions) {
            helperMap.put(v.getName(), v);
        }

        // TODO: find a way to avoid this repetition... (also in constructor)

        var dump = new HashMap<Register, Number>();
        var lstream = LineStream.newInstance(rawRegisterDump);
        while (lstream.peekNextLine() != null) {
            var line = lstream.nextLine();
            if (!SpecsStrings.matches(line, REGPATTERN))
                continue;

            var regAndValue = SpecsStrings.getRegex(line, REGPATTERN);
            var reg = regAndValue.get(0).trim();
            var value = Long.parseUnsignedLong(regAndValue.get(1).trim(), 16);
            var key = helperMap.get(reg);
            if (key != null)
                dump.put(key, value);
        }
        return dump;
    }

    @Override
    public Number getValue(Register registerDef) {
        return this.regvalues.get(registerDef);
    }

    /*
    @Override
    public Number getValue(String registerName) {
        return this.regvalues.get(this.helperMap.get(registerName));
    }*/

    @Override
    public String toString() {
        var sBuilder = new StringBuilder();

        var sortedMap = new TreeMap<Register, Number>();
        sortedMap.putAll(this.regvalues);
        int mod = sortedMap.keySet().size() / 3;

        int i = 1;
        for (var key : sortedMap.keySet()) {
            sBuilder.append(key + ": 0x" + Long.toHexString(sortedMap.get(key).longValue()));
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
    public Map<Register, Number> getRegisterMap() {
        return regvalues;
    }
}

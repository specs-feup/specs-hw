package pt.up.fe.specs.binarytranslation.producer.detailed;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.LineStream;

public class RegisterDump {

    private HashMap<String, Long> regs;
    // public static RegisterDump nullDump = new RegisterDumpNull();

    public RegisterDump() {
        this.regs = new HashMap<>();
    }

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

    public Long getValue(String register) {
        return regs.get(register);
    }

    public void prettyPrint() {
        HashMap<String, Long> sortedMap = this.regs.entrySet().stream()
                .sorted(Entry.comparingByValue())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                        (e1, e2) -> e1, HashMap::new));
        int mod = sortedMap.keySet().size() / 3;
        int i = 1;
        for (String key : sortedMap.keySet()) {
            System.out.print(key + ": " + sortedMap.get(key));
            System.out.print(i % mod == 0 ? "\n" : ", ");
            i++;
        }
    }

    public boolean isEmpty() {
        return regs.size() == 0;
    }

    public RegisterDump copy() {
        RegisterDump copyDump = new RegisterDump();
        for (String key : regs.keySet()) {
            copyDump.add(key, regs.get(key));
        }
        return copyDump;
    }

    public void merge(RegisterDump mergeRegs) {
        for (var m : mergeRegs.getRegisterMap().keySet()) {
            regs.put(m, mergeRegs.getValue(m));
        }
    }

    public HashMap<String, Long> getRegisterMap() {
        return regs;
    }
}

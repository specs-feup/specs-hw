package pt.up.fe.specs.binarytranslation.producer.detailed;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class RegisterDump {
    HashMap<String, Long> regs = new HashMap<>();

    public void add(String register, long value) {
        regs.put(register, value);
    }

    public long getValue(String register) {
        return regs.get(register);
    }
    
    public void prettyPrint() {
        HashMap<String, Long> sortedMap = 
                this.regs.entrySet().stream()
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
}

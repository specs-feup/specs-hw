/**
 * Copyright 2020 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.simulator.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pt.up.fe.specs.simulator.Addr;
import pt.up.fe.specs.simulator.MemoryElement;
import pt.up.fe.specs.util.SpecsLogs;

public class GenericMemoryElement implements MemoryElement {

    private final String name;
    private final Map<Addr, Number> memory;

    private Map<Addr, String> addrNames;

    public GenericMemoryElement(String name) {
        this.name = name;
        this.memory = new HashMap<>();

        addrNames = null;
    }

    public void setAddrNames(Map<Addr, String> addrNames) {
        this.addrNames = addrNames;
    }

    @Override
    public Number read(Addr address) {
        var value = memory.get(address);

        return value;
    }

    @Override
    public Number write(Addr address, Number value) {
        return memory.put(address, value);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {

        List<Addr> keys = new ArrayList<>(memory.keySet());
        Collections.sort(keys);

        return keys.stream().map(key -> getAddrString(key) + ": " + memory.get(key))
                .collect(Collectors.joining("\n"));
    }

    private String getAddrString(Addr key) {
        if (addrNames != null) {
            var addrString = addrNames.get(key);

            if (addrString == null) {
                SpecsLogs.info("Addr names is set, but there is no mapping for address " + key);
                addrString = key.getHexString();
            }

            return addrString;
        }

        return key.getHexString();
    }

}

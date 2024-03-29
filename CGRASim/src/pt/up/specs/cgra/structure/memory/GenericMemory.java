/**
 * Copyright 2021 SPeCS.
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

package pt.up.specs.cgra.structure.memory;

import java.util.HashMap;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEInteger;

public class GenericMemory implements Memory {

    private int memsize;
    private HashMap<PEInteger, PEData> mem;

    public GenericMemory(int memsize) {
        this.memsize = memsize;
        this.mem = new HashMap<PEInteger, PEData>();
    }

    @Override
    public void reset() {
        this.mem.clear();
    }

    @Override
    public PEData read(PEInteger addr) {
        return this.mem.get(addr);
    }

    @Override
    public boolean write(PEInteger addr, PEData data) {
        if (addr.getValue().intValue() > this.memsize)
            return false;
        this.mem.put(addr, data);
        SpecsLogs.debug("GenericMemory: Data stored into generic memory: " + data + " at address " + addr);
        return true;
    }

    public int getSize() {
        return this.memsize;
    }
}

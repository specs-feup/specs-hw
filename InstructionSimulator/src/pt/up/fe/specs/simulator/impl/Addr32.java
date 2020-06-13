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

import pt.up.fe.specs.simulator.Addr;

public class Addr32 implements Addr {

    private final Integer addr;

    public Addr32(Integer addr) {
        this.addr = addr;
    }

    @Override
    public Number toNumber() {
        return addr;
    }

    @Override
    public int getBitwidth() {
        return 32;
    }

    @Override
    public Addr add(Number value) {
        return new Addr32(addr + value.intValue());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((addr == null) ? 0 : addr.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Addr32 other = (Addr32) obj;
        if (addr == null) {
            if (other.addr != null)
                return false;
        } else if (!addr.equals(other.addr))
            return false;
        return true;
    }

    @Override
    public int compareTo(Addr o) {
        return addr.compareTo(o.toNumber().intValue());
    }

}

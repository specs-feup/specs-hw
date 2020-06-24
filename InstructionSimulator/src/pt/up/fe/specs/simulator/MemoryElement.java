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

package pt.up.fe.specs.simulator;

import pt.up.fe.specs.util.SpecsLogs;

public interface MemoryElement {

    String getName();

    Number read(Addr address);

    Number write(Addr address, Number value);

    default Number readSafe(Addr address) {

        var value = read(address);

        if (value == null) {
            SpecsLogs.debug(() -> "Accessing uninitialized memory '" + getName() + "' at address "
                    + address);
            value = 0;
        }

        return value;
    }

    // default Number read(Enum<?> register) {
    // return read(register.ordinal());
    // }

    // default Number write(Enum<?> register, Number value) {
    // return write(register.ordinal(), value);
    // }

}

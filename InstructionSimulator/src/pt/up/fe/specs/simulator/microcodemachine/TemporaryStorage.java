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

package pt.up.fe.specs.simulator.microcodemachine;

import java.util.HashMap;
import java.util.Map;

public class TemporaryStorage {

    private Map<String, Number> temporaryStorage;

    public TemporaryStorage() {
        this.temporaryStorage = new HashMap<>();
    }

    public boolean has(Enum<?> id) {
        return has(id.name());
    }

    public boolean has(String id) {
        return temporaryStorage.containsKey(id);
    }

    public Number read(String id) {
        var value = temporaryStorage.get(id);

        if (value == null) {
            throw new RuntimeException("No temporary value set of id '" + id + "'");
        }

        return value;
    }

    public Number readAndClear(Enum<?> id) {
        return readAndClear(id.name());
    }

    public Number readAndClear(String id) {
        var value = read(id);

        // Clear value
        temporaryStorage.remove(id);

        return value;
    }

    public Number write(Enum<?> id, Number value) {
        return write(id.name(), value);
    }

    public Number write(String id, Number value) {
        return temporaryStorage.put(id, value);
    }

}

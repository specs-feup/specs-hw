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

import pt.up.fe.specs.util.SpecsStrings;

/**
 * Represents an address of a machine (e.g., 32-bit or 64-bit number)
 * 
 * @author jbispo
 *
 */
public interface Addr extends Comparable<Addr> {

    Number toNumber();

    int getBitwidth();

    Addr add(Number value);

    default String getHexString() {
        return SpecsStrings.toHexString(toNumber().longValue(), getBitwidth() / 4);
    }

    /**
     * Increments the address by a single step.
     * 
     * <p>
     * The default implementation assumes addresses that are byte-addressed.
     * 
     * @return
     */
    default Addr inc() {
        return add(getBitwidth() / 8);
    }

}

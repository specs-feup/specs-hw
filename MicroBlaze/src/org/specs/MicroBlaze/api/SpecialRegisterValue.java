/**
 * Copyright 2015 SPeCS.
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

package org.specs.MicroBlaze.api;

import org.specs.MicroBlaze.MbRegister;

public interface SpecialRegisterValue {

    /**
     * 
     * @return the most significant bit of the value
     */
    int getMsb();

    /**
     * 
     * @return the number of bits of the value
     */
    int getSize();

    /**
     * 
     * @return the MicroBlaze register of this value
     */
    MbRegister getRegister();
}

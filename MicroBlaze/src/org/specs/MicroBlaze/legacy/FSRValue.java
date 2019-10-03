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

package org.specs.MicroBlaze.legacy;

;

/**
 * Floating Point Status Register.
 * 
 * <p>
 * Functions return the MSB of the corresponding flag. Using functions instead of constants to avoid problems with
 * constant replacement in byte-code.
 * 
 * @author JoaoBispo
 *
 */
public enum FSRValue implements SpecialRegisterValue {

    IO(27),
    DZ(28),
    OF(29),
    UF(30),
    DO(31);

    private final int msb;

    private FSRValue(int msb) {
        this.msb = msb;
    }

    @Override
    public int getMsb() {
        return msb;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public MbRegister getRegister() {
        return MbRegister.RFSR;
    }
}

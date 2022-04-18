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

package pt.up.fe.specs.binarytranslation.instruction.register;

import java.util.List;

public interface Register {

    public String getName();

    public List<RegisterType> getRegTypes();

    default boolean isProgramCounter() {
        return this.getRegTypes().contains(RegisterType.PROGRAMCOUNTER);
    }

    default boolean isParameter() {
        return this.getRegTypes().contains(RegisterType.PARAMETER);
    }

    default boolean isReturn() {
        return this.getRegTypes().contains(RegisterType.RETURNVALUE);
    }

    default boolean isStackPointer() {
        return this.getRegTypes().contains(RegisterType.STACKPOINTER);
    }

    default boolean isFramePointer() {
        return this.getRegTypes().contains(RegisterType.FRAMEPOINTER);
    }

    default boolean isTemporary() {
        return this.getRegTypes().contains(RegisterType.TEMPORARY);
    }

    default boolean isZero() {
        return this.getRegTypes().contains(RegisterType.HARDZERO);
    }
}

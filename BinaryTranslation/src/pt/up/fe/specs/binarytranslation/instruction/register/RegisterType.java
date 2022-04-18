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

public enum RegisterType {

    PROGRAMCOUNTER,
    GENERALPURPOSE,
    TEMPORARY,
    STACKPOINTER,
    FRAMEPOINTER,

    // TODO: INTEGER, FIXED, and FLOAT types??

    RETURNADDR, // TODO: same as link register??
    GLOBALPOINTER, // specific to riscv?
    THREADPOINTER,
    SAVED, // "saved registers" in riscv... TODO: what does that mean??

    SYSCALL, // only applicable to aarch64???
    RETURNVALUE,
    PARAMETER,
    HARDZERO,
    SPECIAL
}

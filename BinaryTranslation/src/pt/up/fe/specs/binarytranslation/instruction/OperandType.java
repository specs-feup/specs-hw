/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.binarytranslation.instruction;

/**
 * Generic types of operands common to all ISAs (in theory)
 * 
 * @author NunoPaulino
 *
 */
public enum OperandType {

    // types
    REGISTER,
    IMMEDIATE,
    SYMBOLIC,

    // access
    READ,
    WRITE,

    // data type
    INTEGER,
    FLOAT,
    SIMD,

    // data sizes
    NIBBLE,
    BYTE,
    HALFWORD,
    WORD,
    DWORD,
    QWORD,

    // representational
    SPECIAL, // e.g. special registers, like stack pointer or system registers, have this subtype
    SUBOPERATION;
    // SUBOPERATION?? --> expresses things to do to the registers in the shifted register operations for ARM?

    // DIRECT
    // INDIRECT
    // POINTER (?)
    //
}

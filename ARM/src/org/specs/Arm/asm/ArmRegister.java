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

package org.specs.Arm.asm;

import static pt.up.fe.specs.binarytranslation.instruction.register.RegisterType.*;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.register.Register;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterType;

public enum ArmRegister implements Register {

    // https://wiki.cdot.senecacollege.ca/wiki/AArch64_Register_and_Instruction_Quick_Start
    // "r0 through r30 - to refer generally to the registers"
    // "x0 through x30 - for 64-bit-wide access (same registers)"
    // TODO CHECK if r == x ??

    // https://developer.arm.com/documentation/ddi0595/2021-06/AArch64-Registers
    // https://developer.arm.com/documentation/dui0801/a/Overview-of-AArch64-state/Predeclared-core-register-names-in-AArch64-state
    // https://developer.arm.com/documentation/den0024/a/ARMv8-Registers/NEON-and-floating-point-registers/Scalar-register-sizes

    // "In AArch64 state, the PC is not a general purpose register and you cannot access it by name."

    // TODO: maybe the "r" == "w" prefix equality can be resolved in the @ArmRegisterResolver
    // (which still doesn't exist...)

    // 32bit GP bank (with "w" prefix)
    WZR("wzr", HARDZERO),
    WSP("wsp", STACKPOINTER),
    // W31("w31", STACKPOINTER), TODO: w31 is "special"... its stack pointer for some instructions, and a hardzero for
    // others...

    W0("w0", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    W1("w1", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    W2("w2", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    W3("w3", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    W4("w4", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    W5("w5", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    W6("w6", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    W7("w7", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    W8("w8", GENERALPURPOSE, SYSCALL),
    W9("w9", GENERALPURPOSE, TEMPORARY),
    W10("w10", GENERALPURPOSE, TEMPORARY),
    W11("w11", GENERALPURPOSE, TEMPORARY),
    W12("w12", GENERALPURPOSE, TEMPORARY),
    W13("w13", GENERALPURPOSE, TEMPORARY),
    W14("w14", GENERALPURPOSE, TEMPORARY),
    W15("w15", GENERALPURPOSE, TEMPORARY),
    W16("w16", GENERALPURPOSE, SPECIAL),
    W17("w17", GENERALPURPOSE, SPECIAL),
    W18("w18", GENERALPURPOSE, SPECIAL),
    // these 3 specials are said to be used for "intra-procedure-call and platform values (avoid)"
    // TODO: im not sure exactly what that means!

    W19("w19", GENERALPURPOSE),
    W20("w20", GENERALPURPOSE),
    W21("w21", GENERALPURPOSE),
    W22("w22", GENERALPURPOSE),
    W23("w23", GENERALPURPOSE),
    W24("w24", GENERALPURPOSE),
    W25("w25", GENERALPURPOSE),
    W26("w26", GENERALPURPOSE),
    W27("w27", GENERALPURPOSE),
    W28("w28", GENERALPURPOSE),
    W29("w29", GENERALPURPOSE, FRAMEPOINTER),
    W30("w30", GENERALPURPOSE, RETURNADDR),

    XZR("xzr", HARDZERO);

    // TODO: all others!! (specials, other sizes , etc etc... SIMD registers...)

    private final String name;
    private final List<RegisterType> regTypes;

    private ArmRegister(String name, RegisterType... registerTypes) {
        this.name = name;
        this.regTypes = Arrays.asList(registerTypes);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<RegisterType> getRegTypes() {
        return regTypes;
    }
}

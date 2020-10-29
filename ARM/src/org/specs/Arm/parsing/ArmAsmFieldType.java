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

package org.specs.Arm.parsing;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;

public enum ArmAsmFieldType implements AsmFieldType {

    // 1.
    DPI_PCREL,
    DPI_ADDSUBIMM,
    DPI_ADDSUBIMM_TAGS,
    LOGICAL,
    MOVEW,
    BITFIELD,
    EXTRACT,

    // 2.
    CONDITIONALBRANCH,
    EXCEPTION,
    HINTS,
    BARRIER,
    SYSREGMOVE,
    UCONDITIONALBRANCH_REG,
    UCONDITIONALBRANCH_IMM,
    COMPARE_AND_BRANCH_IMM,
    TEST_AND_BRANCH,

    // 3.
    LOAD_REG_LITERAL_FMT1,
    LOAD_REG_LITERAL_FMT2,
    LOAD_STORE_PAIR_NO_ALLOC,

    LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1,
    LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2,

    LOAD_STORE_REG_IMM_PREPOST_FMT1,
    LOAD_STORE_REG_IMM_PREPOST_FMT2,
    LOAD_STORE_REG_IMM_PREPOST_FMT3,

    LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT1,
    LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT2,
    LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT3,

    LOAD_STORE_REG_OFF_FMT1,
    LOAD_STORE_REG_OFF_FMT2,
    LOAD_STORE_REG_OFF_FMT3,

    LOAD_STORE_REG_UIMM_FMT1,
    LOAD_STORE_REG_UIMM_FMT2,
    LOAD_STORE_REG_UIMM_FMT3,

    // 4. Data Processing (Register) (C4.1.5) /////////////////////////////////
    DPR_TWOSOURCE,
    DPR_ONESOURCE,
    LOGICAL_SHIFT_REG,
    ADD_SUB_SHIFT_REG,
    ADD_SUB_EXT_REG,
    ADD_SUB_CARRY,
    CONDITIONAL_CMP_REG,
    CONDITIONAL_CMP_IMM,
    CONDITIONAL_SELECT,
    DPR_THREESOURCE,

    // 5. Data Processing -- Scalar Floating-Point and Advanced SIMD (C4.1.6) //////////////////
    // TODO: SIMD instructions
    FP_DPR_ONESOURCE,
    FP_COMPARE,
    FP_IMMEDIATE,
    FP_COND_COMPARE,
    FP_DPR_TWOSOURCE,
    FP_COND_SELECT,
    FP_DPR_THREESOURCE,

    UNDEFINED;
}

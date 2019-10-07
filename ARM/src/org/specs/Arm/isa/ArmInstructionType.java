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

package org.specs.Arm.isa;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;

public enum ArmInstructionType implements AsmInstructionType {

    DPI_PCREL,
    DPI_ADDSUBIMM,
    DPI_ADDSUBIMM_TAGS,
    LOGICAL,
    MOVEW,

    BRANCH,
    LOAD_STORES,
    DPR,
    UNDEFINED;
}
/*
DATA_PROCESSING_REGISTER,
MISCELLANEOUS_1,
DATA_PROCESSING_REGISTER_SHIFT,
MISCELLANEOUS_2,
MULTIPLIES_AND_EXTRA_LOAD_STORE,    
UNDEFINED_INSTRUCTION,
MOVE_IMMEDOATE_TO_STATUS_REGISTER,
LOAD_STORE_IMMEDIATE_OFFSET,
MEDIA_INSTRUCTION,
ARCHITECTURALLY_UNDEFINED,
LOAD_STORE_MULTIPLE,
COPROCESSOR_LOAD_STORE_AND_DOUBLE_REGISTER_TRANSFERS,
COPROCESSOR_DATA_PROCESSING,
COPROCESSOR_REGISTER_TRANSFERS,
SOFTWARE_INTERRUPT,
UNCONDITION_INSTRUCTION,
UNPREDICTABLE,*/

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

package org.specs.MicroBlaze.isa;

import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;

public enum MicroBlazeAsmFieldType implements AsmFieldType {

    SPECIAL,
    MBAR,
    UBRANCH,
    ULBRANCH,
    UIBRANCH,
    UILBRANCH,
    CBRANCH,
    CIBRANCH,
    BARREL,
    IBARREL_FMT1,
    IBARREL_FMT2,
    RETURN,
    STREAM,
    DSTREAM,
    IMM,
    TYPE_A,
    TYPE_B,
    UNDEFINED
}

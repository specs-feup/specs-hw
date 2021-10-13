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
 
package pt.up.fe.specs.binarytranslationlauncher.options;

// public enum BinaryTranslationISAList implements StringProvider {
public enum BinaryTranslationISAList {

    MicroBlaze32Bit("MicroBlaze (32 bit)"),
    ARMaarch64("ARM (aarch64)"),
    RISCVrisc32iam("RISC-V (risc32iam)");

    // private static final Lazy<EnumHelperWithValue<BinaryTranslationISAList>> ENUM_HELPER = EnumHelperWithValue
    // .newLazyHelperWithValue(BinaryTranslationISAList.class);

    private String value;

    private BinaryTranslationISAList(String name) {
        this.value = name;
    }

    public String getValue() {
        return value;
    }

    // public static BinaryTranslationISAList getEnum(String value) {
    // return ENUM_HELPER.get().fromValue(value);
    // }
    //
    // @Override
    // public String getString() {
    // return value;
    // }

}

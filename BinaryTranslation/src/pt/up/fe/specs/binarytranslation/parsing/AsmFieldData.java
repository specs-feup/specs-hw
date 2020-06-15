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

package pt.up.fe.specs.binarytranslation.parsing;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

import pt.up.fe.specs.util.SpecsCheck;

/**
 * Raw field data as extracted by an {@link IsaParser}
 * 
 * @author NunoPaulino
 *
 */
public class AsmFieldData extends ADataClass<AsmFieldData> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7457507990835654072L;

    /*
     * Addr of this instruction
     */
    public static final DataKey<Number> ADDR = KeyFactory.object("addr", Number.class);

    /*
     * This datakey only holds the instruction type (i.e., binary instruction format as specified in the parsers)
     */
    public static final DataKey<AsmFieldType> TYPE = KeyFactory.object("type", AsmFieldType.class);

    /*
     * This map contains the field names specified in the parsers, and their values
     */
    public static final DataKey<Map<String, String>> FIELDS = KeyFactory.generic("fields",
            (Map<String, String>) new LinkedHashMap<String, String>());

    /*
     * Constructor
     */
    public AsmFieldData(Number addr, AsmFieldType type, Map<String, String> fields) {
        set(ADDR, addr);
        set(TYPE, type);
        set(FIELDS, fields);
    }

    /*
     * 
     */
    public Number getAddr() {
        return this.get(ADDR);
    }

    /*
     * Get type of instruction as defined by parsers, and in implementations of AsmInstructionType
     */
    public AsmFieldType getType() {
        return this.get(TYPE);
    }

    /*
     * Get all fields
     */
    public Map<String, String> getFields() {
        return this.get(FIELDS);
    }

    /*
     * Reduces all fields with prefix name "opcode" to a single sequence of bits
     */
    public int getReducedOpcode() {
        String tmp = "";
        var map1 = this.get(FIELDS);
        var keys1 = map1.keySet();

        for (String key : keys1) {
            if (key.contains("opcode")) {
                tmp = tmp + map1.get(key);
            }
        }

        if (tmp.isEmpty())
            return 0;

        return Integer.parseInt(tmp, 2);
    }

    public int getFieldAsBinaryInteger(String fieldName) {
        var valueString = get(AsmFieldData.FIELDS).get(fieldName);
        SpecsCheck.checkNotNull(valueString, () -> "No value found for field " + fieldName);
        return Integer.parseInt(valueString, 2);
    }
}

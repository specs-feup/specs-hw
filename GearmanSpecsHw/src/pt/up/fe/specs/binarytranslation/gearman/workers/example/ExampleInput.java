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
 
package pt.up.fe.specs.binarytranslation.gearman.workers.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExampleInput {
    private String field1;
    private String anotherField;

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        string.append("field1: " + field1 + "\n");
        string.append("anotherField: " + anotherField + "\n");

        return string.toString();
    }

    public String getField1() {
        return field1;
    }

    public String getAnotherField() {
        return anotherField;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public void setAnotherField(String anotherField) {
        this.anotherField = anotherField;
    }

    /**
     * New instance of {@link ExampleInput} based on the bytes array of a json object
     * 
     * @param data
     *                 the json object byte array
     * @return
     */
    public static ExampleInput newInstance(byte[] data) {
        String dataString = new String(data);
        Gson gson = new GsonBuilder().create();
        ExampleInput exampleInput = gson.fromJson(dataString, ExampleInput.class);
        return exampleInput;
    }

}

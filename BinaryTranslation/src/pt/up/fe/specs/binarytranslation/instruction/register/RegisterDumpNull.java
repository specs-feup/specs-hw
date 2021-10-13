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

import java.util.Map;

public class RegisterDumpNull implements RegisterDump {

    public RegisterDumpNull() {
    }

    @Override
    public Number getValue(Register registerName) {
        return 0;
    }

    @Override
    public void prettyPrint() {
        System.out.println("Register dump is empty");
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Map<Register, Number> getRegisterMap() {
        return null;
    }

    @Override
    public RegisterDump copy() {
        return this;
    }
}

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

public interface RegisterDump {

    /*
     * get value by register definition (i.e. enum), such as MicroBlazeRegister.R1
     */
    public Number getValue(Register registerName);

    /*
     * get value by register string name, such as "r1"
     */
    // public Number getValue(String registerName);

    /*
     * 
     */
    public void prettyPrint();

    /*
     * 
     */
    public boolean isEmpty();

    /*
     * 
     */
    public Map<Register, Number> getRegisterMap();

    /*
     * 
     */
    public RegisterDump copy();
}

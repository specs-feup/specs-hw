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
 
package pt.up.fe.specs.binarytranslation.instruction;

import java.lang.reflect.Method;

public class SimpleInstruction {

    /*
     * Helper class to hold a dumbed down instruction
     * so as to consume less memory. Instructions can be rebuilt
     * by reparsing calling the constructor that originated them,
     * after frequent hashed sequences are determined
     */
    private String address;
    private String instruction;
    private Method c;

    public SimpleInstruction(Instruction i) {
        this.address = Long.toHexString(i.getAddress().longValue());
        this.instruction = i.getInstruction();
        try {
            this.c = i.getClass().getMethod("newInstance", String.class, String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getAddress() {
        return address;
    }

    public String getInstruction() {
        return instruction;
    }

    public Instruction rebuild() {
        Instruction i = null;
        try {
            i = (Instruction) this.c.invoke(null, this.address, this.instruction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return i;
    }
}

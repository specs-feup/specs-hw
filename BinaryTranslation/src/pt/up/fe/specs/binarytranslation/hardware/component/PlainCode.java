/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.binarytranslation.hardware.component;

import java.io.OutputStream;
import java.util.*;

public class PlainCode implements HardwareComponent {

    /*
     * Just string of code (useful for stuff i havent moved into its own class yet)
     */
    List<String> code;

    public PlainCode(List<String> code) {
        this.code = code;
    }

    public PlainCode(String code) {
        this.code = Arrays.asList(code);
    }

    public PlainCode(String... code) {
        this.code = Arrays.asList(code);
    }

    @Override
    public String getAsString() {
        return String.join("\n", this.code);
    }

    @Override
    public void emit(OutputStream os) {
        // TODO Auto-generated method stub
    }
}
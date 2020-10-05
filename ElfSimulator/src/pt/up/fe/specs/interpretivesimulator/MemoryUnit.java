/**
 *  Copyright 2020 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.interpretivesimulator;

public class MemoryUnit {
    private final String name;
    private int value;
    private boolean read;
    private boolean write;
    
    public MemoryUnit(String name) {
        this.name = name;
        this.read = true;
        this.write = true;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isReadable() {
        return read;
    }

    public void setReadable(boolean read) {
        this.read = read;
    }

    public boolean isWritable() {
        return write;
    }

    public void setWritable(boolean write) {
        this.write = write;
    }
}

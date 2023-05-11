/**
 * Copyright 2023 SPeCS.
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

package pt.up.specs.cgra.dataypes;

public abstract class APEData implements PEData {

    @Override
    public Number getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PEData copy() {
        // TODO Auto-generated method stub
        return null;
    }
    /*
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PEInteger))
            return false;
    
        else if (obj == this)
            return true;
    
        return (this.getValue() == ((PEInteger) obj).value);// TODO: unsafe
    }*/
}

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

package pt.up.specs.cgra.structure.pes;

import pt.up.specs.cgra.control.PEControl;
import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.dataypes.PEData;

public abstract class ACProcessingElement<T extends PEControlSetting> extends AProcessingElement
        implements PEControl<T> {

    @Override
    protected abstract PEData _execute();

    @Override
    public boolean setControl(int i) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PEControlSetting getControl() {
        // TODO Auto-generated method stub
        return null;
    }

}

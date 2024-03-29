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

package pt.up.specs.cgra.control;

/**
 * This interface defines the methods that a PE needs to implement if it needs/requires control bits (e.g. setting which
 * operation to perform in an ALU, or setting a coefficient in a DSP-like operation)
 * 
 * @author nuno
 *
 */
public interface PEControl<T extends PEControlSetting> {

    public boolean setOperation(T ctrl);

    public T getOperation();
}

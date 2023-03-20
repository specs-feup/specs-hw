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
 
package pt.up.specs.cgra.structure.interconnect;

import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.context.Context;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

public interface Interconnect {

    /*
     * 
     */
    public SpecsCGRA getCGRA();

    /**
     * Propagates data to @ProcessingElement inputs based on interconnect settings
     */
    public boolean propagate();

    /*
     * Set an individual connection in the internconnect
     */
    public boolean setConnection(ProcessingElementPort from, ProcessingElementPort to);

    /*
     * Apply an entire connection context to the interconnect
     */
    public boolean applyContext(Context ctx);

    /*
     * get current interconnection context
     */
    public Context getContext();

    /*
     * 
     */
    public ProcessingElementPort findDriver(ProcessingElementPort to);

    /*
     * 
     */
    public boolean connectionValid(ProcessingElementPort from, ProcessingElementPort to);
    
    public Context makeContext(int id);
}

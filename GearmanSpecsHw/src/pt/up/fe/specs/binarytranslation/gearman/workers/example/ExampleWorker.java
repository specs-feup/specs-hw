/**
 * Copyright 2016 SPeCS.
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

import org.gearman.GearmanFunctionCallback;

import pt.up.fe.specs.binarytranslation.gearman.SpecsHwWorker;

public class ExampleWorker extends SpecsHwWorker {

    public ExampleWorker() {
        super();
    }

    @Override
    public void setUp() {
        // Setup before running
        // E.g., generate/clean an output folder, if needed
    }

    /* (non-Javadoc)
     * @see org.specs.Gearman.utils.WorkerWithTimeout#tearDown()
     */
    @Override
    public void tearDown() {
        // Cleanup after running
        // E.g., deleting an output folder
    }

    /**
     * What the worker should execute.
     */
    @Override
    public byte[] workInternal(String function, byte[] data, GearmanFunctionCallback callback) throws Exception {

        // Parse input json
        ExampleInput clavaInput = ExampleInput.newInstance(data);

        // Do stuff
        String[] array = new String[1];
        array[0] = "Stored " + clavaInput.getAnotherField();

        // Create output data
        ExampleOutput clavaOutput = new ExampleOutput(3, "Read field " + clavaInput.getField1(), true, array);

        return clavaOutput.getJsonBytes();
    }

    /**
     * What should be returned when an error occurs.
     */
    @Override
    protected byte[] getErrorOutput(String message) {
        return ExampleOutput.getErrorOutput("[SpecsHw] " + message);
    }

}

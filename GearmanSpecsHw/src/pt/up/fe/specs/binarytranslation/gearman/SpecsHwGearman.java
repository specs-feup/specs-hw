/**
 * Copyright 2013 SPeCS Research Group.
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

package pt.up.fe.specs.binarytranslation.gearman;

import java.util.concurrent.TimeUnit;

import org.gearman.Gearman;
import org.gearman.GearmanFunction;
import org.gearman.GearmanServer;
import org.gearman.GearmanWorker;

import pt.up.fe.specs.binarytranslation.gearman.workers.example.ExampleWorker;
import pt.up.fe.specs.gearman.GearmanUtils;
import pt.up.fe.specs.gearman.specsworker.GenericSpecsWorker;
import pt.up.fe.specs.gearman.utils.GearmanSecurityManager;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;

/**
 * @author Joao Bispo
 * 
 */
public class SpecsHwGearman {

    /**
     * @param args
     */
    private static final int MAX_WORKERS = 5;
    private static final long TIMEOUT = 120;
    private static final TimeUnit TIMEUNIT = TimeUnit.SECONDS;

    private static final String TIMEOUT_MESSAGE = "Terminated task after exceeding the maximum alloted time of "
            + SpecsHwGearman.TIMEOUT + SpecsStrings.getTimeUnitSymbol(SpecsHwGearman.TIMEUNIT);

    public static String getTimeoutMessage() {
        return SpecsHwGearman.TIMEOUT_MESSAGE;
    }

    public static long getTimeout() {
        return SpecsHwGearman.TIMEOUT;
    }

    public static TimeUnit getTimeunit() {
        return SpecsHwGearman.TIMEUNIT;
    }

    public static void main(String[] args) {

        SpecsSystem.programStandardInit();

        // Set SecurityManager to catch potential System.exit() call from workers
        System.setSecurityManager(new GearmanSecurityManager());

        // Create a new gearman system using 8 threads
        final Gearman gearman = Gearman.createGearman();

        // Create a GearmanServer on port 4733, to avoid conflits with already running gearman jobs on the same machine
        GearmanServer server = GearmanUtils.newServer(gearman, 4733, args);

        // Create workers that will serve client requests
        for (int i = 0; i < SpecsHwGearman.MAX_WORKERS; i++) {

            // Create a GearmanWorker object
            final GearmanWorker worker = gearman.createGearmanWorker();
            worker.addServer(server);

            worker.addFunction("Example", new ExampleWorker());

            var localFunctionWorker = newLocalFunctionWorker();
            worker.addFunction(localFunctionWorker.getWorkerName(), localFunctionWorker);
        }

    }

    private static GenericSpecsWorker newLocalFunctionWorker() {
        return new GenericSpecsWorker("LocalFunction", localFunction(),
                message -> message, SpecsHwGearman.TIMEOUT, SpecsHwGearman.TIMEUNIT);
    }

    public static GearmanFunction localFunction() {
        return (function, data, callback) -> {
            // Just execute something, and return nothing
            return new byte[0];
        };
    }
}

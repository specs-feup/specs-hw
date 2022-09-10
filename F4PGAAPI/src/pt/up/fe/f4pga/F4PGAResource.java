/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.f4pga;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum F4PGAResource implements ResourceProvider {

    F4PGAJSON_TEMPLATE("pt/up/fe/f4pga/flow.json");
    F4PGABASH_SRICPT_TEMPLATE("pt/up/fe/f4pga/build.sh");

    private final String resource;

    private F4PGAResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}

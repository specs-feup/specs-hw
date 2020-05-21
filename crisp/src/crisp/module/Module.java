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

package crisp.module;

import java.util.*;

import crisp.port.Port;

public class Module {

    String name;
    final List<Port> ports;

    // TODO: ... a module may have other modules inside...

    public Module(String name, Port... ports) {
        this.name = name;
        this.ports = new ArrayList<Port>(Arrays.asList(ports));
    }

    public Module(Port... ports) {
        this.ports = new ArrayList<Port>(Arrays.asList(ports));
    }

    Port getPort(int idx) {
        return this.ports.get(idx);
    }

    public String getName() {
        return name;
    }

    /// write module?

    // method "define"? use some kind of lamba expression or set of them?
}

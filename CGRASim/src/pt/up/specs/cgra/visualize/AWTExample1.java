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

package pt.up.specs.cgra.visualize;

import java.awt.Button;
import java.awt.Frame;

public class AWTExample1 {

    private Frame f;
    private Button btn;

    public AWTExample1() {
        this.f = new Frame();
        this.btn = new Button("Hello World");
        btn.setBounds(80, 80, 100, 50);
        f.add(btn); // adding a new Button.
        f.setSize(300, 250); // setting size.
        f.setTitle("JavaTPoint"); // setting title.
        f.setLayout(null); // set default layout for frame.
    }

    public void setVisible() {
        f.setVisible(true); // set frame visibility true.
    }
}

import java.io.*;

import pt.up.fe.specs.binarytranslation.BinaryTranslationUtils;
import pt.up.fe.specs.guihelper.*;
import pt.up.fe.specs.guihelper.Base.SetupDefinition;
import pt.up.fe.specs.guihelper.gui.SimpleGui;
import pt.up.fe.specs.util.*;
import pt.up.fe.specs.util.properties.SpecsProperty;

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

public class BinaryTranslationLauncher implements AppDefaultConfig, AppSource {

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SpecsSystem.programStandardInit();
        /*
        for (var resource : getResources()) {
            SpecsIo.resourceCopy(resource, SpecsIo.getWorkingDir(), true, false);
        }
        */
        SpecsProperty.ShowStackTrace.applyProperty("true");

        // Start log file
        try (PrintStream stream = new PrintStream(new FileOutputStream(new File("mbextractor.log")))) {
            SpecsLogs.addLog(stream);

            if (GuiHelperUtils.trySingleConfigMode(args, new BinaryTranslationLauncher())) {
                return;
            }

            App app = new BinaryTranslationLauncher();
            SimpleGui gui = new SimpleGui(app);

            gui.setTitle("Binary Translation Framework" + BinaryTranslationUtils.getCommitDescription());
            gui.execute();

        } catch (Exception e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        }
    }

    @Override
    public int execute(File setupFile) throws InterruptedException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public SetupDefinition getEnumKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public App newInstance() {
        return new BinaryTranslationLauncher();
    }

    @Override
    public String defaultConfigFile() {
        // TODO Auto-generated method stub
        return null;
    }
}

package pt.up.fe.specs.binarytranslationlauncher;

import java.util.Arrays;

import org.suikasoft.jOptions.JOptionsUtils;
import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.app.AppKernel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsSystem;

public class BinaryTranslationLauncher implements App {

    public static void main(String[] args) {
        SpecsSystem.programStandardInit();

        JOptionsUtils.executeApp(new BinaryTranslationLauncher(), Arrays.asList(args));
    }

    @Override
    public String getName() {
        return "Binary Translation Framework " + BinaryTranslationUtils.getCommitDescription();
    }

    @Override
    public AppKernel getKernel() {
        return new BinaryTranslation();
    }

    @Override
    public StoreDefinition getDefinition() {
        return BinaryTranslation.getDefinition();
    }
}

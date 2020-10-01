package pt.up.fe.specs.binarytranslationlauncher;

import java.io.File;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppKernel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionBuilder;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

public class BinaryTranslation implements AppKernel {

    public static final DataKey<File> ELF_FILE = KeyFactory.file("ELFFile").setLabel("ELF file");

    @Override
    public int execute(DataStore options) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static StoreDefinition getDefinition() {
        var builder = new StoreDefinitionBuilder(BinaryTranslation.class);

        // stream options
        var opts1 = StoreDefinitions.fromInterface(BinaryTranslationStreamData.class);
        builder.addNamedDefinition("Instruction Stream Options", opts1);

        // detection options
        var opts2 = StoreDefinitions.fromInterface(BinaryTranslationDetectionData.class);
        builder.addNamedDefinition("Segment Detection Options", opts2);

        // graph options (?)...

        // hardware generation options (?)...

        return builder.build();
    }
}

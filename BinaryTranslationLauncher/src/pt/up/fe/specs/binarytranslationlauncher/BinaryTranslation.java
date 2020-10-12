package pt.up.fe.specs.binarytranslationlauncher;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppKernel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionBuilder;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

public class BinaryTranslation implements AppKernel {

    @Override
    public int execute(DataStore options) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static StoreDefinition getDefinition() {
        var builder = new StoreDefinitionBuilder(BinaryTranslation.class);

        // IO options
        var opts0 = StoreDefinitions.fromInterface(BinaryTranslationIOData.class);
        builder.addNamedDefinition("Input Options", opts0);

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

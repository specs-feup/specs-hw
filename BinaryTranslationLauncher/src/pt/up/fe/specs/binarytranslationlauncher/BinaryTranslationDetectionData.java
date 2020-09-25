package pt.up.fe.specs.binarytranslationlauncher;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

public class BinaryTranslationDetectionData extends ADataClass<BinaryTranslationStreamData> {

    public static final DataKey<Boolean> FREQUENTSEQUENCE = KeyFactory.bool("Frequent Sequence");
    public static final DataKey<Boolean> BASICBLOCK = KeyFactory.bool("Basic Block");
}

package pt.up.fe.specs.binarytranslationlauncher;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Utils.EnumCodec;

import pt.up.fe.specs.binarytranslationlauncher.options.BinaryTranslationISAList;

public class BinaryTranslationStreamData extends ADataClass<BinaryTranslationStreamData> {

    public static final DataKey<BinaryTranslationISAList> INSTRUCTIONSET = KeyFactory
            .enumeration("Instruction Set", BinaryTranslationISAList.class)
            .setDefault(() -> BinaryTranslationISAList.MicroBlaze32Bit)
            .setDecoder(new EnumCodec<BinaryTranslationISAList>(BinaryTranslationISAList.class,
                    anEnum -> anEnum.getValue()));

    public static final DataKey<Boolean> STATICANALYSIS = KeyFactory.bool("Static Analysis");
    public static final DataKey<Boolean> SIMULATION = KeyFactory.bool("Simulation");
}

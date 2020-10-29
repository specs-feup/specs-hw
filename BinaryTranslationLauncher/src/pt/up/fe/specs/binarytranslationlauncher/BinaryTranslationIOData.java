package pt.up.fe.specs.binarytranslationlauncher;

import java.io.File;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

public class BinaryTranslationIOData extends ADataClass<BinaryTranslationIOData> {

    public static final DataKey<File> ELFFILE = KeyFactory.file("ELFFile").setLabel("ELF file");
    public static final DataKey<File> OUTPUTFOLDER = KeyFactory.file("OutputFolder").setLabel("Output Folder");
    public static final DataKey<Boolean> CLEANOUTPUT = KeyFactory.bool("CleanOutputFolder");
}

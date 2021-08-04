package pt.up.fe.specs.binarytranslation;

import java.io.File;
import java.util.function.Function;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.providers.ResourceProvider;

public interface ELFProvider extends ResourceProvider {

    default public String asTxtDump() {
        var name = this.getELFName().replace(".elf", "_objdump.txt");
        return name;
    }

    default public String asTraceTxtDump() {
        var name = this.getELFName().replace(".elf", "_trace.txt");
        return name;
    }

    default public Application toApplication() {
        throw new NotImplementedException("toApplication()");
    }

    public String getELFName();

    public Number getKernelStart();

    public Number getKernelStop();

    default File getFile() {
        return this.write();
    }

    @Override
    default File write(File folder, boolean overwrite, Function<String, String> nameMapper) {

        var unzipFolder = SpecsIo.getTempFolder("unzip_" + SpecsIo.removeExtension(getResourceName()));

        // copy of zip on disk (outside jar/bin)
        var zipFile = SpecsIo.resourceCopy(this.getResource(), unzipFolder);
        BinaryTranslationUtils.unzip(zipFile, getELFName(), unzipFolder);

        var unzippedFile = new File(unzipFolder, getELFName());

        var finalFile = new File(folder, unzippedFile.getName());

        if (finalFile.isFile() && !overwrite) {
            return finalFile;
        }

        SpecsIo.copy(unzippedFile, finalFile);
        finalFile.deleteOnExit();
        return finalFile;
    }

    @Override
    default String read() {
        var file = write(SpecsIo.getTempFolder());
        return SpecsIo.read(file);
    }
}

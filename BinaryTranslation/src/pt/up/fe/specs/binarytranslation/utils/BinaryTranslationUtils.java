package pt.up.fe.specs.binarytranslation.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import pt.up.fe.specs.binarytranslation.BinaryTranslationResource;
import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.processes.ProcessRun;
import pt.up.fe.specs.specshw.SpecsHwUtils;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;

public class BinaryTranslationUtils {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    // file
    public static File getFile(String path) {
        File fd = SpecsIo.resourceCopy(path);
        fd.deleteOnExit();
        return fd;
    }

    // file
    public static File getFile(ELFProvider elf) {
        File fd = SpecsIo.resourceCopy(elf.getResource());
        fd.deleteOnExit();
        return fd;
    }

    /*
     * Gets file out of zip by resource name, when getResource is called
     */
    public static void unzip(File zipFile, String filename, File outputFolder) {
        var args = new ArrayList<String>();
        var progname = "7z";
        if (IS_WINDOWS)
            progname += ".exe";
        args.add(progname);
        args.add("e");
        args.add("-aoa"); // add "aoa" overrides without prompt
        // args.add("-aos"); // add "aos" to avoid overwrite
        args.add(zipFile.getAbsolutePath());
        args.add("-o" + outputFolder.getAbsolutePath());
        args.add(filename);
        var unzipResult = SpecsSystem.runProcess(args, true, false);
        // var unzipOutput = unzipResult.getOutput();
        // System.out.println(BinaryTranslationUtils.getAllOutput(new ProcessBuilder(args)));

        if (unzipResult.isError()) {
            throw new RuntimeException("Error while unzipping file '" + zipFile.getAbsolutePath() + "' to folder '"
                    + outputFolder.getAbsolutePath() + "':\n" + unzipResult.getOutput());
        }

        // return !unzipResult.isError();
    }

    /**
     * Dumps the output of the stdio thread of any @ProcessRun to a given file
     * 
     * @author nuno
     *
     */
    public static void dumpProcessRunStdOut(ProcessRun proc, String filename) {

        try {
            var fos = new FileWriter(filename);
            var bw = new BufferedWriter(fos);

            String line = null;
            while ((line = proc.receive(100)) != null) {
                bw.write(line + "\n");
                bw.flush();
            }
            bw.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Dump plain bytes into a given filename
     */
    public static void bytesToFile(File filename, byte[] data) {

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            BufferedOutputStream bw = new BufferedOutputStream(fos);
            bw.write(data);
            bw.flush();
            bw.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Generate JSON from any object
     */
    public static void toJSON(String filename, Object obj) {
        var f = SpecsIo.mkdir(filename);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        var bytes = gson.toJson(obj).getBytes();
        BinaryTranslationUtils.bytesToFile(f, bytes);
    }

    /*
     * Generate given object from JSON bytes
     */
    public static Object fromJSON(String filename, Class<?> className) {

        Object obj = null;
        try {
            obj = new Gson().fromJson(new FileReader(filename), className);

        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return obj;
    }

    /*
     * 
     */
    public static void renderDotty(String pngName, String dottyContents) {
        var dotfile = new File(pngName.replace(".png", ".dot"));
        dotfile.deleteOnExit();
        SpecsIo.write(dotfile, dottyContents);
        BinaryTranslationUtils.renderDotty(dotfile);
    }

    /*
     * Render the dotty file into a PNG file (calls dot executable)
     */
    public static void renderDotty(File dotfile) {

        var dotpath = dotfile.getAbsolutePath();
        var pngpath = dotpath.replace(".dot", ".png");

        // render dotty
        var arguments = Arrays.asList(BinaryTranslationResource.DOTTY_BINARY.getResource(),
                "-Tpng", dotfile.getAbsolutePath(), "-o", pngpath);

        // dot -Tps filename.dot -o outfile.ps
        var pb = new ProcessBuilder(arguments);
        SpecsHwUtils.newProcess(pb);
    }

    /*
     * 
     */
    public static int signExtend16(int value, int currlen) {
        return (value << (16 - currlen)) >> (16 - currlen);
    }

    /*
     * 
     */
    public static int signExtend32(int value, int currlen) {
        return (value << (32 - currlen)) >> (32 - currlen);
    }

    /*
     * 
     */
    public static long signExtend64(int value, int currlen) {
        return (value << (64 - currlen)) >> (64 - currlen);
    }
}

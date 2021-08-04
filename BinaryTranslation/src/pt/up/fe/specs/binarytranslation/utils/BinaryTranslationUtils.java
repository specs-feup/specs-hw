package pt.up.fe.specs.binarytranslation.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import pt.up.fe.specs.binarytranslation.BinaryTranslationResource;
import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.processes.ProcessRun;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.Replacer;

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
        args.add("7z");
        args.add("e");
        args.add("-aos");
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
    public static Process newProcess(ProcessBuilder builder) {

        // start gdb
        Process proc = null;
        try {
            builder.directory(new File("."));
            builder.redirectErrorStream(true); // redirects stderr to stdout
            proc = builder.start();

        } catch (IOException e) {
            throw new RuntimeException("Could not run process: " + builder.command());
        }

        return proc;
    }

    /*
     * 
     */
    private static BufferedReader getOutputStreamReader(ProcessBuilder pb) {

        // call program
        Process proc = null;
        try {
            pb.directory(new File("."));
            pb.redirectErrorStream(true); // redirects stderr to stdout
            proc = pb.start();

        } catch (IOException e) {
            throw new RuntimeException("Could not run process bin with name: " + pb.command());
        }

        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        return reader;
    }

    /*
     * 
     */
    private static String getSingleOutputLine(ProcessBuilder pb) {

        String output = null;
        BufferedReader reader = getOutputStreamReader(pb);
        try {
            output = reader.readLine();
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return output;
    }

    /*
     * 
     */
    private static String getAllOutput(ProcessBuilder pb) {

        String output = "";
        BufferedReader reader = getOutputStreamReader(pb);
        try {
            while (reader.ready())
                output += reader.readLine() + "\n";
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return output;
    }

    /*
     * Gets "git describe" result for .. which project?? (could be BinaryTranslation, MicroBlaze...)
     */
    public static String getCommitDescription() {

        // call git describe
        var arguments = Arrays.asList("git", "describe");
        ProcessBuilder pb = new ProcessBuilder(arguments);
        return BinaryTranslationUtils.getSingleOutputLine(pb);
    }

    /*.
     * Output compilation flags for a given elf, using a given variant of a GNU based "readelf"
     */
    public static String getCompilationInfo(File elfname, String readelbinary) {

        // assume that windows doesnt have tools
        // if (BinaryTranslationUtils.IS_WINDOWS)
        // return "GNU tools unavailable to extract compilation information!";

        // call readelf
        List<String> arguments = null;
        if (IS_WINDOWS) {
            readelbinary += ".exe";
            var internalcmd = readelbinary + " -wi "
                    + elfname.getAbsolutePath() + "| findstr /irc:DW_AT_producer";
            arguments = Arrays.asList("cmd.exe", "/C", internalcmd);

        } else {
            var internalcmd = readelbinary + " -wi "
                    + elfname.getAbsolutePath() + " | grep -i compilation -A6";
            arguments = Arrays.asList("/bin/bash", "-c", internalcmd);
        }

        ProcessBuilder pb = new ProcessBuilder(arguments);
        var output = BinaryTranslationUtils.getAllOutput(pb);
        Pattern pat = Pattern.compile("GNU.*");
        Matcher mat = pat.matcher(output);
        if (mat.find())
            return mat.group(0);
        else
            return "Could not retrieve build information!";
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
        ProcessBuilder pb = new ProcessBuilder(arguments);
        BinaryTranslationUtils.newProcess(pb);
    }

    /*
     * Get SPeCS copyright text with current year
     */
    public static String getSPeCSCopyright() {
        ResourceProvider crtext = BinaryTranslationResource.SPECS_COPYRIGHT_TEXT;
        var crreplacer = new Replacer(crtext);
        crreplacer.replace("<THEYEAR>", LocalDateTime.now().getYear());
        return crreplacer.toString();
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

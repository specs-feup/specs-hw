/**
 * Copyright 2021 SPeCS.
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

package pt.up.fe.specs.specshw;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;

import pt.up.fe.specs.util.utilities.Replacer;

public class SpecsHwUtils {

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
        var pb = new ProcessBuilder(arguments);
        return SpecsHwUtils.getSingleOutputLine(pb);
    }

    /*
     * Get contact info and github repo stuff
     */
    public static String getContactInfo() {
        var info = new StringBuilder();
        info.append("Available on GitHub at https://github.com/specs-feup/specs-hw\n");
        info.append("Contacts:\n\tNuno Paulino (nuno.m.paulino@inesctec.pt)\n\tJoão Bispo (jbispo@fe.up.pt)");
        return info.toString();
    }

    /*
     * Get SPeCS copyright text with current year
     */
    public static String getSPeCSCopyright() {
        var crtext = SpecsHwResource.SPECS_COPYRIGHT_TEXT;
        var crreplacer = new Replacer(crtext);
        crreplacer.replace("<THEYEAR>", LocalDateTime.now().getYear());
        return crreplacer.toString();
    }

    /*
     * Helper
     */
    public static String generateFileHeader() {
    
       var commentText = new StringBuilder();
        commentText.append(SpecsHwUtils.getSPeCSCopyright() + "\n\n");
        commentText.append("Generated using CrispyHDL "
                + "(a part of the specs-hw GitHub Repo " + SpecsHwUtils.getCommitDescription() + ")\n");
        commentText.append(SpecsHwUtils.getContactInfo());
        return commentText.toString();
        
    }
}

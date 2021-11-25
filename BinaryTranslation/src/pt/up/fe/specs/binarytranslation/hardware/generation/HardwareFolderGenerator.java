/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.hardware.generation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import pt.up.fe.specs.util.SpecsLogs;

public class HardwareFolderGenerator {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
    
    private static final String directorySeparator = IS_WINDOWS ? "\\" : "/";
    
    public static final String folderHW = "hw";
    public static final String folderHW_HDL = folderHW + directorySeparator + "hdl";
    public static final String folderHW_TESTBENCHES = folderHW + directorySeparator + "tb";
    public static final String folderHW_SYNTHESIS = folderHW + directorySeparator + "syn";
    public static final String folderHW_REPORTS = folderHW + directorySeparator + "reports";
    
    private static final List<String> folders = List.of(folderHW, folderHW_HDL, folderHW_TESTBENCHES, folderHW_SYNTHESIS, folderHW_REPORTS); 
    
    public static void generate(String path) {

        try {
            Files.createDirectory(Paths.get(path));
        }catch(FileAlreadyExistsException e) {
            System.out.println("WARNING: Folder " + path + " already exists");
        } catch (IOException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        }
        
        HardwareFolderGenerator.folders.forEach(folder -> {
            try {
                Files.createDirectory(Paths.get(path + directorySeparator + folder));
            }catch(FileAlreadyExistsException e) {
                System.out.println("WARNING: Folder " + path + directorySeparator + folder + " already exists");
            } catch (IOException e) {
                SpecsLogs.msgWarn("Error message:\n", e);
            }
        });
        
    }
    
    public static String getDirectorySeparator() {
        return directorySeparator;
    }
    
    public static String getHardwareFolder(String path) {
        return path + directorySeparator + folderHW;
    }
    
    public static String getHardwareHDLFolder(String path) {
        return path + directorySeparator + folderHW_HDL;
    }
    
    private static FileOutputStream newFile(String path, String fileName, String format) throws IOException {
        File newFile = new File(path + directorySeparator + fileName + "." + format); 
        
        if(!newFile.createNewFile()) {
            System.out.println("\nWARNING: File " + fileName + "." + format +" already exists, it will be overwritten");
        }
         
         return new FileOutputStream(path + directorySeparator  + fileName + "." + format);
    }
    
    public static FileOutputStream newHardwareHDLFile(String path, String fileName, String format) throws IOException {
        return HardwareFolderGenerator.newFile(HardwareFolderGenerator.getHardwareHDLFolder(path), fileName, format);
    }
    
    public static String getHardwareTestbenchesFolder(String path) {
        return path + directorySeparator + folderHW_TESTBENCHES;
    }
    
    public static FileOutputStream newHardwareTestbenchFile(String path, String fileName, String format) throws IOException {
        return HardwareFolderGenerator.newFile(HardwareFolderGenerator.getHardwareTestbenchesFolder(path), fileName, format);
    }
    
    public static String getHardwareSynthesisFolder(String path) {
        return path + directorySeparator + folderHW_SYNTHESIS;
    }
    
    public static FileOutputStream newHardwareSynthesisFile(String path, String fileName, String format) throws IOException {
        return HardwareFolderGenerator.newFile(HardwareFolderGenerator.getHardwareSynthesisFolder(path), fileName, format);
    }
    
    public static String getHardwareReportsFolder(String path) {
        return path + directorySeparator + folderHW_REPORTS;
    }
    
    public static FileOutputStream newHardwareReportsFile(String path, String fileName, String format) throws IOException {
        return HardwareFolderGenerator.newFile(HardwareFolderGenerator.getHardwareReportsFolder(path), fileName, format);
    }
}

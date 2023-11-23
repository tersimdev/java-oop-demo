package util.helpers;

import java.io.FileWriter;

import java.io.IOException;

import util.Log;

/**
 * <p>
 * An helper class for facilitating the writing of Strings to files.
 * Adheres to Single Responsibility Principle
 * </p>
 * 
 * @author Lim Jun Rong Ryan
 * @version 1.0
 * @since 23-11-2023
 */

public class FileWriterHelper {

    public static void writeToFile(String fileName, String fileContent) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(fileContent.toString());
        } catch (IOException exception) {
            Log.error("Failed to write " + fileName);
        }
    }
}
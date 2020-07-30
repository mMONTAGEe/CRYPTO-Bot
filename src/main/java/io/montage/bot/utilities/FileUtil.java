package io.montage.bot.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import io.montage.bot.Bot;



public class FileUtil {

    public static String readFromFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Bot.log("{}", e);
        }
        return null;
    }
    
    public static int readIntFromFile(File file) {
        try {
            String s = FileUtils.readFileToString(file, Charset.defaultCharset()).replaceAll("[^\\p{Graph}\n\r\t ]", "");;
            Bot.log(s);
            return Integer.parseInt(s);
        } catch (IOException e) {
            Bot.log("{}", e);
        }
        return -1;
    }

    public static void writeToFile(File file, String content) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File createFileIfNotExist(File file) {
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return file;
    }

}

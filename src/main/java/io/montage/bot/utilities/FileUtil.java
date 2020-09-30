package io.montage.bot.utilities;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

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
		} catch (FileNotFoundException e) {}
		return null;
	}

	public static int readIntFromFile(File file) {
		try {
			new Charsets();
			String s = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			return Integer.parseInt(s);
		} catch (IOException e) {

		}
		return -1;
	}

	public static File createFileIfNotExist(File file) {
		try {
			boolean nuser = file.createNewFile();
			if (nuser) {
				FileWriter writer = new FileWriter(file);
				writer.write("1");
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static void handleXPDataFile(File file, boolean delete) {
		if (!delete) {
			try {
				FileUtil.createFileIfNotExist(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (delete) {
			try {
				FileUtils.forceDelete(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

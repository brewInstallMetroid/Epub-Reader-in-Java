package edu.unomaha.epubreader.io;

import java.io.File;

public class FileValidator {
	public static boolean isValidEpub(File file) {
		return file.getName().endsWith(".epub");
	}
}

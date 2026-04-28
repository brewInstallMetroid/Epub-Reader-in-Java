package edu.unomaha.epubreader.io;

import java.io.File;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FileChooserTest {
	@Test
	void testValidFile() {
		assertTrue(FileValidator.isValidEpub(new File("DraculaBramStoker.epub")));
		assertFalse(FileValidator.isValidEpub(new File("Sample.txt")));
	}
}

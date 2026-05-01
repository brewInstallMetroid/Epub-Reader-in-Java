package edu.unomaha.epubreader.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;

import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;

public class ChapterResourceTest {
	@Test
	public void testFlattenedToc() throws Exception {
		File epub = new File("DraculaBramStoker.epub");
		EpubReader reader = new EpubReader();
		Book book = reader.readEpub(new FileInputStream(epub.getAbsolutePath()));

		ReaderController controller = new ReaderController();

		List<TOCReference> flat = new ArrayList<>();
		controller.collectToc(book.getTableOfContents().getTocReferences(), flat);

		assertFalse(flat.isEmpty());
		assertTrue(flat.size() >= book.getTableOfContents().getTocReferences().size());

		for (TOCReference ref : flat) {
			assertNotNull(ref.getTitle());
			assertNotNull(ref.getResource());
			assertNotNull(ref.getResource().getHref());
		}
	}
}

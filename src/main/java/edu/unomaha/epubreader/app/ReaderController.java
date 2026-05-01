package edu.unomaha.epubreader.app;

import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.web.WebView;

import javafx.scene.control.ListView;
import javafx.collections.FXCollections;


public class ReaderController {
	public ListView<String> chapterList;
	public WebView webView;
	private List<TOCReference> toc;
	private Book book;
	private List<SpineReference> spine;

	public void load(Book book) {
		this.book = book;
		this.spine = book.getSpine().getSpineReferences();
		populate();
		setupHandler();

		chapterList.setItems(FXCollections.observableArrayList(spine.stream()
					.map(ref -> getChapterTitle(ref)).toList()));
		chapterList.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null || newVal.intValue() < 0) return;
			loadChapter(newVal.intValue());
		});
	}

	private void setupHandler() {
		chapterList.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null || newVal.intValue() < 0) return;
			loadChapter(newVal.intValue());
		});
	}

	public String getChapterTitle(SpineReference ref) {
		String href = ref.getResource().getHref();
		return book.getTableOfContents().getTocReferences()
			.stream()
			.filter(toc -> toc.getResource() != null && href.equals(toc.getResource().getHref()))
			.map(TOCReference::getTitle)
			.findFirst()
			.orElse(href.substring(href.lastIndexOf('/') + 1));
	}

	private void populate() {
		List<TOCReference> flat = new ArrayList<>();
		collectToc(book.getTableOfContents().getTocReferences(), flat);
		this.toc = flat;
		chapterList.setItems(FXCollections.observableArrayList(
			flat.stream().map(TOCReference::getTitle).toList()
		));
	}

	public void loadChapter(int index) {
		try {
			Resource res = toc.get(index).getResource();
			String html = new String(res.getData(), res.getInputEncoding());
			webView.getEngine().loadContent(html, "text/html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void collectToc(List<TOCReference> list, List<TOCReference> out) {
		for (TOCReference ref : list) {
			out.add(ref);
			if (ref.getChildren() != null && !ref.getChildren().isEmpty()) {
				collectToc(ref.getChildren(), out);
			}
		}
	}

}

package edu.unomaha.epubreader.app;

import edu.unomaha.epubreader.io.FileValidator;

import java.io.File;
import java.io.FileInputStream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.domain.Book;

public class AppMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            ListView<String> sideMenu = new ListView<>();
            Label toolLabel = new Label("MENU");

            Button openButton = new Button("Open");
            FileChooser fiCh = new FileChooser();
            fiCh.setTitle("Open EPUB to read");
            fiCh.setInitialDirectory(new File(System.getProperty("user.dir")));

            Label mainLabel = new Label();
            Label titleLabel = new Label();
            Label authorLabel = new Label();

            Separator sep0 = new Separator();

            ToolBar headerBar = new ToolBar(toolLabel, sep0, openButton);

            BorderPane layout = new BorderPane();
            WebView webView = new WebView();
            sideMenu.setMaxWidth(150);
            layout.setTop(headerBar);
            layout.setLeft(sideMenu);
            layout.setCenter(webView);

            VBox infoVBox = new VBox(mainLabel, titleLabel, authorLabel);
            infoVBox.setPadding(new Insets(8));
            infoVBox.setStyle("-fx-border-color: #555;");
            layout.setRight(infoVBox);

            ReaderController controller = new ReaderController();
            controller.chapterList = sideMenu;
            controller.webView = webView;

            openButton.setOnAction(e -> {
                File selectedEpub = fiCh.showOpenDialog(primaryStage);
                if (selectedEpub != null && FileValidator.isValidEpub(selectedEpub)) {
                    mainLabel.setText("Selected File:\n" + selectedEpub.getName());
                    try {
                        EpubReader reader = new EpubReader();
                        Book book = reader.readEpub(new FileInputStream(selectedEpub));
                        titleLabel.setText("Title: " + book.getTitle());
                        authorLabel.setText("Author: " + book.getMetadata().getAuthors());
                        controller.load(book);

                    } catch (Exception ex) {
                        showError("File I/O Error: " + ex.getMessage());
                    }
                } else {
                    mainLabel.setText("FILE INVALID!\nMust be an EPUB\n" + selectedEpub);
                }
            });

            Scene sidebarScene = new Scene(layout, 900, 600);

            Label label = new Label("Welcome to my EPUB Display App!");
            Button beginButton = new Button("BEGIN");
            beginButton.setOnAction(e -> primaryStage.setScene(sidebarScene));

            VBox root = new VBox(10, label, beginButton);
            root.setStyle("-fx-padding: 20; -fx-alignment: center;");

            Scene scene = new Scene(root, 300, 200);
            primaryStage.setTitle("EPUB-DISPLAYER");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception ex) {
            showError("Startup Error: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("App Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


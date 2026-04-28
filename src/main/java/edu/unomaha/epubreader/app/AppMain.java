package edu.unomaha.epubreader.app;

import edu.unomaha.epubreader.io.FileValidator;

import java.io.File;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class AppMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// NOTE:: Setup for main scene with top and sidebar
			ListView<String> sideMenu = new ListView<>();
			Label toolLabel = new Label("MENU");

			Button openButton = new Button("Open");
			FileChooser fiCh = new FileChooser();
			fiCh.setTitle("Open EPUB to read");
			fiCh.setInitialDirectory(new File(System.getProperty("user.dir")));
			Label mainLabel = new Label("Press Open on the top bar to select a file!");
			openButton.setOnAction(e -> {
				File selectedEpub = fiCh.showOpenDialog(primaryStage);
				if (FileValidator.isValidEpub(selectedEpub) && selectedEpub != null) {
					mainLabel.setText("Selected File::\n" + selectedEpub.getPath());
				} else {
					mainLabel.setText("FILE INVALID!\nMust be an Epub\n" + selectedEpub.getPath());
				}
			});
			Button pageIncButton = new Button("Next Page");
			Button pageDecButton = new Button("Previous Page");

			Separator sep0 = new Separator();
			Separator sep1 = new Separator();
			Separator sep2 = new Separator();
			
			sideMenu.getItems().addAll("Book sections\nwill go\nhere!");
			ToolBar headerBar = new ToolBar(toolLabel, sep0, openButton, sep1, pageDecButton, sep2, pageIncButton);
			BorderPane layout = new BorderPane();
			sideMenu.setMaxWidth(100);
			layout.setTop(headerBar);
			layout.setLeft(sideMenu);
			layout.setCenter(mainLabel);
			Scene sidebarScene = new Scene(layout, 700, 400);


			// NOTE:: Setup for intro scene with begin button
			Label label = new Label("Welcome to my EPUB Display App!");
			Button beginButton = new Button("BEGIN");
			beginButton.setOnAction(e -> {
				try {
					primaryStage.setScene(sidebarScene);
					primaryStage.show();
				} catch (Exception ex) {
					showError("An unexpected error occured: " + ex.getMessage());
				}
			});
			VBox root = new VBox(10, label, beginButton);
			root.setStyle("-fx-padding: 20; -fx-alignment: center;");

			
			// NOTE:: Inital stage instantiation
			Scene scene = new Scene(root, 300, 200);
			primaryStage.setTitle("EPUB-DISPLAYER");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception ex) {
			showError("An unexpected error occured: " + ex.getMessage());
		}
	}
	


	private void showError(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("ERROR:");
		alert.setHeaderText("App Error");
		alert.setContentText(message);
		alert.showAndWait();	
	}



	public static void main(String[] args) {
		launch(args);
	}
	//public static void main(String[] args) {
	//	System.out.println("Welcome to my EPUB Display App!");
	//}
}

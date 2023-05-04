package ru.itlc.testproject.clientside.components;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.itlc.testproject.clientside.responses.Book;
import ru.itlc.testproject.clientside.utils.HttpWorkUtils;

import java.util.Arrays;

public class MainView {
	
	private Stage stage;
	private BookTableView table;
	
	public MainView() {
		buildUI();
	}

	private void buildUI() {
		stage = new Stage(StageStyle.DECORATED);

		BorderPane root = new BorderPane();

		table = new BookTableView();

		Book[] book = HttpWorkUtils.getAllBooks();
		if (book != null) {
			Arrays.stream(book).forEach(b -> table.add(b));
		}

		root.setCenter(table);

		Scene scene = new Scene(root, 1200, 400);
		scene.getStylesheets().add("ru/itlc/testproject/clientside/stylesheet.css");
		stage.setTitle("TableView Demo");
		stage.setScene(scene);
		stage.show();
	}
}

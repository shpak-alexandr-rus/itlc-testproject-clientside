package ru.itlc.testproject.clientside.components;

import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.itlc.testproject.clientside.responses.Book;
import ru.itlc.testproject.clientside.responses.BooleanResponse;
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

		AnchorPane root = new AnchorPane();

		Button deleteBtn = new Button("Удалить");

		deleteBtn.setPrefWidth(125.0);
		Button editBtn = new Button("Редактировать");
		editBtn.setPrefWidth(125.0);
		Button addBtn = new Button("Добавить");
		addBtn.setPrefWidth(125.0);

		table = new BookTableView();

		deleteBtn.disableProperty().bind(Bindings.isEmpty(table.getTableView().getSelectionModel().getSelectedItems()));
		deleteBtn.setOnAction(e -> {
			BooleanResponse response = HttpWorkUtils.deleteBookById(table.getTableView().getSelectionModel().getSelectedItems().get(0).getBookId());
			if (response != null && response.isStatus()) {
				table.clear();
				Book[] book = HttpWorkUtils.getAllBooks();
				if (book != null) {
					Arrays.stream(book).forEach(b -> table.add(b));
				}
			}
		});
		editBtn.disableProperty().bind(Bindings.isEmpty(table.getTableView().getSelectionModel().getSelectedItems()));
		editBtn.setOnAction(e -> {
			System.out.println("Editing");
		});

		AnchorPane.setTopAnchor(table, 10.0);
		AnchorPane.setLeftAnchor(table, 10.0);
		AnchorPane.setRightAnchor(table, 145.0);
		AnchorPane.setBottomAnchor(table, 10.0);

		AnchorPane.setTopAnchor(deleteBtn, 10.0);
		AnchorPane.setRightAnchor(deleteBtn, 10.0);

		AnchorPane.setTopAnchor(editBtn, 110.0);
		AnchorPane.setRightAnchor(editBtn, 10.0);

		AnchorPane.setTopAnchor(addBtn, 210.0);
		AnchorPane.setRightAnchor(addBtn, 10.0);


		Book[] book = HttpWorkUtils.getAllBooks();
		if (book != null) {
			Arrays.stream(book).forEach(b -> table.add(b));
		}

		root.getChildren().add(table);
		root.getChildren().add(deleteBtn);
		root.getChildren().add(editBtn);
		root.getChildren().add(addBtn);
//		root.setCenter(table);

		Scene scene = new Scene(root, 1200, 400);
		scene.getStylesheets().add("ru/itlc/testproject/clientside/stylesheet.css");
		stage.setTitle("TableView Demo");
		stage.setScene(scene);
		stage.show();
	}
}

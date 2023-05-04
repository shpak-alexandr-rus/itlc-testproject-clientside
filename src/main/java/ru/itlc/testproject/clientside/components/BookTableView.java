package ru.itlc.testproject.clientside.components;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.LongStringConverter;
import ru.itlc.testproject.clientside.responses.Book;

public class BookTableView extends VBox {
	
	private TableView<Book> table;
	private TableColumn<Book, Long> bookId;
	private TableColumn<Book, String> bookAuthor;
	private TableColumn<Book, String> bookTitle;
	private TableColumn<Book, String> bookPublisher;
	private TableColumn<Book, String> bookPublisherAddress;
	private TableColumn<Book, String> bookPublishingDate;

	public BookTableView() {
		buildUI();
	}

	private void buildUI() {
		
		table = new TableView<>();
//		table.setEditable(true);

		bookId = new TableColumn<>("Номер в каталоге");
		bookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
		bookId.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));

		bookAuthor = new TableColumn<>("Авторы");
		bookAuthor.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
		bookAuthor.setCellFactory(TextFieldTableCell.forTableColumn());

		bookTitle = new TableColumn<>("Название");
		bookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
		bookTitle.setCellFactory(TextFieldTableCell.forTableColumn());

		bookPublisher = new TableColumn<>("Издательство");
		bookPublisher.setCellValueFactory(new PropertyValueFactory<>("bookPublisher"));
		bookPublisher.setCellFactory(TextFieldTableCell.forTableColumn());

		bookPublisherAddress = new TableColumn<>("Адрес издательства");
		bookPublisherAddress.setCellValueFactory(new PropertyValueFactory<>("bookPublisherAddress"));
		bookPublisherAddress.setCellFactory(TextFieldTableCell.forTableColumn());

		bookPublishingDate = new TableColumn<>("Название");
		bookPublishingDate.setCellValueFactory(new PropertyValueFactory<>("bookPublishingDate"));
		bookPublishingDate.setCellFactory(TextFieldTableCell.forTableColumn());

		table.getColumns().add(bookId);
		table.getColumns().add(bookAuthor);
		table.getColumns().add(bookTitle);
		table.getColumns().add(bookPublisher);
		table.getColumns().add(bookPublisherAddress);
		table.getColumns().add(bookPublishingDate);

		
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		getChildren().add(table);
	}
	
	public void add(Book book) {
		table.getItems().add(book);
	}

	public TableView<Book> getTableView() {
		return table;
	}
}

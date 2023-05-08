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
	// Колонка таблицы для хранения ID
	private TableColumn<Book, Long> bookId;
	// Колонка таблицы для хранения автора книги
	private TableColumn<Book, String> bookAuthor;
	// Колонка таблицы для хранения названия книги
	private TableColumn<Book, String> bookTitle;
	// Колонка таблицы для хранения названия издательства
	private TableColumn<Book, String> bookPublisher;
	// Колонка таблицы для хранения адреса издательства
	private TableColumn<Book, String> bookPublisherAddress;
	// Колонка таблицы для хранения даты публикации книги
	private TableColumn<Book, String> bookPublishingDate;

	public BookTableView() {
		buildUI();
	}

	private void buildUI() {
		// Создание таблицы
		table = new TableView<>();

		// Создание колонки номера в каталоге
		bookId = new TableColumn<>("Номер в каталоге");
		bookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
		bookId.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));

		// Создание колонки автора книги
		bookAuthor = new TableColumn<>("Авторы");
		bookAuthor.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
		bookAuthor.setCellFactory(TextFieldTableCell.forTableColumn());

		// Создание колонки названия книги
		bookTitle = new TableColumn<>("Название");
		bookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
		bookTitle.setCellFactory(TextFieldTableCell.forTableColumn());

		// Создание колонки названия издательства
		bookPublisher = new TableColumn<>("Издательство");
		bookPublisher.setCellValueFactory(new PropertyValueFactory<>("bookPublisher"));
		bookPublisher.setCellFactory(TextFieldTableCell.forTableColumn());

		// Создание колонки адреса издательства
		bookPublisherAddress = new TableColumn<>("Адрес издательства");
		bookPublisherAddress.setCellValueFactory(new PropertyValueFactory<>("bookPublisherAddress"));
		bookPublisherAddress.setCellFactory(TextFieldTableCell.forTableColumn());

		// Создание колонки даты публикации
		bookPublishingDate = new TableColumn<>("Дата публикации");
		bookPublishingDate.setCellValueFactory(new PropertyValueFactory<>("bookPublishingDate"));
		bookPublishingDate.setCellFactory(TextFieldTableCell.forTableColumn());

		// Добавление всех колонок в таблицу
		table.getColumns().add(bookId);
		table.getColumns().add(bookAuthor);
		table.getColumns().add(bookTitle);
		table.getColumns().add(bookPublisher);
		table.getColumns().add(bookPublisherAddress);
		table.getColumns().add(bookPublishingDate);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.getItems().clear();
		getChildren().add(table);
	}

	// Добавить книгу в таблицу
	public void add(Book book) {
		table.getItems().add(book);
	}

	// Взять объект таблицы
	public TableView<Book> getTableView() {
		return table;
	}

	// Очистить содержимое таблицы
	public void clear() {
		table.getItems().clear();
	}
}

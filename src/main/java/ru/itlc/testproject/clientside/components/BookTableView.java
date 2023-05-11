package ru.itlc.testproject.clientside.components;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.LongStringConverter;
import ru.itlc.testproject.clientside.responses.Book;
import ru.itlc.testproject.clientside.utils.HttpWorkUtils;

public class BookTableView extends VBox {

	private ComboBox<Integer> pageNumber;
	private static boolean pageNumberUpdating = false;
	private ComboBox<Integer> pageSize;
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

		// Создание панели для осуществления процесса пагинации
		GridPane pane = new GridPane();
		HBox paginationBar = new HBox();
		Label paginationDescription = new Label("Вывести  ");
		pageSize = new ComboBox<>();
		pageSize.getItems().add(5);
		pageSize.getItems().add(10);
		pageSize.getItems().add(15);
		pageSize.getItems().add(20);
		pageSize.getSelectionModel().selectFirst();

		pageSize.setOnAction(e -> {
			String sortColumn = null;
			String sortDirection = null;
			if (table.getSortOrder() != null && !table.getSortOrder().isEmpty()) {
				sortColumn = HttpWorkUtils.mapTextToColumnName(table.getSortOrder().get(0).getText());
				sortDirection = HttpWorkUtils.mapSortTypeToDirection(table.getSortOrder().get(0).getSortType().name());
			}
			HttpWorkUtils.refreshBookTable(this, getSelectedPageNumber(), getPageSize(), sortColumn, sortDirection);
		});

		Label pageNumberDescription = new Label("  элементов со страницы  ");
		pageNumber = new ComboBox<>();
		pageNumber.getItems().add(1);
		pageNumber.getSelectionModel().selectFirst();

		pageNumber.setOnAction(e -> {
			if (!pageNumberUpdating) {
				String sortColumn = null;
				String sortDirection = null;
				if (table.getSortOrder() != null && !table.getSortOrder().isEmpty()) {
					sortColumn = HttpWorkUtils.mapTextToColumnName(table.getSortOrder().get(0).getText());
					sortDirection = HttpWorkUtils.mapSortTypeToDirection(table.getSortOrder().get(0).getSortType().name());
				}
				HttpWorkUtils.refreshBookTable(this, getSelectedPageNumber(), getPageSize(), sortColumn, sortDirection);
			}
		});

		paginationBar.getChildren().add(paginationDescription);
		paginationBar.getChildren().add(pageSize);
		paginationBar.getChildren().add(pageNumberDescription);
		paginationBar.getChildren().add(pageNumber);

		pane.getChildren().add(paginationBar);
		pane.setPadding(new Insets(10, 10, 10, 10));

		getChildren().add(table);
		getChildren().add(pane);
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

	public void setPageNumbers(int pageNumberValue) {
		if (!pageNumberUpdating && pageNumber.getItems().size() != pageNumberValue) {
			pageNumberUpdating = true;
			int num = pageNumber.getSelectionModel().getSelectedIndex();
			pageNumber.getItems().clear();
			for (int i = 1; i <= pageNumberValue; i++) {
				pageNumber.getItems().add(Integer.valueOf(i));
			}
			pageNumber.getSelectionModel().select(num > pageNumber.getItems().size() - 1 ? 0 : num);
			String sortColumn = null;
			String sortDirection = null;
			if (table.getSortOrder() != null && !table.getSortOrder().isEmpty()) {
				sortColumn = HttpWorkUtils.mapTextToColumnName(table.getSortOrder().get(0).getText());
				sortDirection = HttpWorkUtils.mapSortTypeToDirection(table.getSortOrder().get(0).getSortType().name());
			}
			HttpWorkUtils.refreshBookTable(this, getSelectedPageNumber(), getPageSize(), sortColumn, sortDirection);
			pageNumberUpdating = false;
		}
	}

	public int getSelectedPageNumber() {
		return pageNumber.getSelectionModel().getSelectedItem() == null ? 1 : pageNumber.getSelectionModel().getSelectedItem();
	}

	public int getPageSize() {
		return pageSize.getSelectionModel().getSelectedItem();
	}
}

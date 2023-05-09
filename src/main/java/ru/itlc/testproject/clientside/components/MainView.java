package ru.itlc.testproject.clientside.components;

import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.itlc.testproject.clientside.responses.Book;
import ru.itlc.testproject.clientside.responses.BookPaginationResponse;
import ru.itlc.testproject.clientside.responses.BooleanResponse;
import ru.itlc.testproject.clientside.utils.HttpWorkUtils;

public class MainView {
	
	private Stage stage;
	private BookTableView table;
	
	public MainView() {
		buildUI();
	}

	private void buildUI() {
		// Создание окна в стиле UTILITY без возможности изменять размер
		stage = new Stage(StageStyle.UTILITY);
		stage.setResizable(false);

		// Создание панели
		AnchorPane root = new AnchorPane();

		// Создание таблицы для отображения записей из базы данных
		table = new BookTableView();

		// Создание обработчика двойного щелчка на элементе таблицы
		table.getTableView().setRowFactory( tv -> {
			TableRow<Book> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					Book book = row.getItem();
					new BookFormView(book);
					refreshBookTable(table, 0, 0, "", "");
				}
			});
			return row ;
		});


		// !!! (НАЧАЛО) СОЗДАНИЕ КНОПОК ГЛАВНОЙ ФОРМЫ !!!
		// Создание кнопки "Удалить"
		Button deleteBtn = new Button("Удалить");
		deleteBtn.setPrefWidth(125.0);

		// Делается кнопка "Удалить" неактивной, если ни одна строка в таблице не выбрана
		deleteBtn.disableProperty().bind(Bindings.isEmpty(table.getTableView().getSelectionModel().getSelectedItems()));

		// Создание обработчика нажатия на кнопку "Удалить"
		deleteBtn.setOnAction(e -> {
			// Выполнение HTTP запроса для удаления записи из базу данных
			BooleanResponse response = HttpWorkUtils.deleteBookById(table.getTableView().getSelectionModel().getSelectedItems().get(0).getBookId());

			// Обновление содержимого таблицы
			if (response != null && response.isStatus()) {
				refreshBookTable(table, 0, 0, "", "");
			}
		});

		// Создание кнопки "Редактировать"
		Button editBtn = new Button("Редактировать");
		editBtn.setPrefWidth(125.0);

		// Делается кнопка "Редактировать" неактивной, если ни одна строка в таблице не выбрана
		editBtn.disableProperty().bind(Bindings.isEmpty(table.getTableView().getSelectionModel().getSelectedItems()));

		// Создание обработчик нажатия на кнопку "Редактировать"
		editBtn.setOnAction(e -> {
			// Создание формы редактирования информации
			new BookFormView(table.getTableView().getSelectionModel().getSelectedItems().get(0));

			// Обновление содержимого таблицы
			refreshBookTable(table, 0, 0, "", "");
		});

		// Создание кнопки "Добавить"
		Button addBtn = new Button("Добавить");
		addBtn.setPrefWidth(125.0);

		// Создание обработчик нажатия на кнопку "Добавить"
		addBtn.setOnAction(e -> {
			// Создание формы редактирования информации
			new BookFormView();
			// Обновление содержимого таблицы
			refreshBookTable(table, 0, 0, "", "");
		});

		// Создание кнопки "Закрыть"
		Button closeBtn = new Button("Закрыть");
		closeBtn.setPrefWidth(125.0);

		// Создаем обработчик нажатия на кнопку "Закрыть"
		closeBtn.setOnAction(event -> stage.close());
		// !!! (КОНЕЦ) СОЗДАНИЕ КНОПОК ГЛАВНОЙ ФОРМЫ !!!


		// !!! (НАЧАЛО) НАСТРОЙКА РАСПОЛЬЖЕНИЯ ЭЛЕМЕНТОВ НА ПАНЕЛЬ !!!
		// Настройка расположения таблицы на панели
		AnchorPane.setTopAnchor(table, 10.0);
		AnchorPane.setLeftAnchor(table, 10.0);
		AnchorPane.setRightAnchor(table, 145.0);
		AnchorPane.setBottomAnchor(table, 10.0);

		// Настройка расположения кнопки "Удалить" на панели
		AnchorPane.setTopAnchor(deleteBtn, 10.0);
		AnchorPane.setRightAnchor(deleteBtn, 10.0);

		// Настройка расположения кнопки "Редактировать" на панели
		AnchorPane.setTopAnchor(editBtn, 110.0);
		AnchorPane.setRightAnchor(editBtn, 10.0);

		// Настройка расположения кнопки "Добавить" на панели
		AnchorPane.setTopAnchor(addBtn, 210.0);
		AnchorPane.setRightAnchor(addBtn, 10.0);

		// Настройка расположения кнопки "Закрыть" на панели
		AnchorPane.setTopAnchor(closeBtn, 310.0);
		AnchorPane.setRightAnchor(closeBtn, 10.0);
		// !!! (КОНЕЦ) НАСТРОЙКА РАСПОЛЬЖЕНИЯ ЭЛЕМЕНТОВ НА ПАНЕЛЬ !!!

		// Обновления содержимого таблицы
		refreshBookTable(table, 1, 5, "book_id", "ASC");

		// !!! (НАЧАЛО) ДОБАВЛЕНИЕ ЭЛЕМЕНТОВ НА ПАНЕЛЬ !!!
		// Добавление таблицу на панель
		root.getChildren().add(table);

		// Добавление кнопки "Удалить" на панель
		root.getChildren().add(deleteBtn);

		// Добавление кнопки "Редактировать" на панель
		root.getChildren().add(editBtn);

		// Добавление кнопки "Добавить" на панель
		root.getChildren().add(addBtn);

		// Добавление кнопки "Закрыть" на панель
		root.getChildren().add(closeBtn);
		// !!! (КОНЕЦ) ДОБАВЛЕНИЕ ЭЛЕМЕНТОВ НА ПАНЕЛЬ !!!

		// Конфигурирование и запуск главного окна
		Scene scene = new Scene(root, 1200, 400);
		scene.getStylesheets().add("ru/itlc/testproject/clientside/stylesheet.css");
		stage.setTitle("Картотека книг");
		stage.setScene(scene);
		stage.show();
	}

	// Метод для обновления содержимого таблицы
	private void refreshBookTable(BookTableView table, int page, int pageSize, String sortingColumn, String sortingDirection) {
		// Очистка содержимого таблицы
		table.clear();

		// Получение данных от сервера
		BookPaginationResponse booksResponse = HttpWorkUtils.getAllBooks(page, pageSize, sortingColumn, sortingDirection);

		// Если данные получены
		if (booksResponse != null) {
			// Данные помещаются в таблицу
			booksResponse.getBooks().forEach(b -> table.add(b));
		}
	}
}

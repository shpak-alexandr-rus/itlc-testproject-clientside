package ru.itlc.testproject.clientside.components;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.itlc.testproject.clientside.responses.Book;
import ru.itlc.testproject.clientside.responses.BooleanResponse;
import ru.itlc.testproject.clientside.utils.HttpWorkUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ru.itlc.testproject.clientside.constants.Constants.LabelTextConstants.DATE_ERROR_MESSAGE;
import static ru.itlc.testproject.clientside.constants.Constants.LabelTextConstants.TEXT_ERROR_MESSAGE;

public class BookFormView {

    private final boolean isEditMode;
    private Book book;

    public BookFormView() {
        isEditMode = false;
        buildUI();
    }

    public BookFormView(Book book) {
        isEditMode = true;
        this.book = book;
        buildUI();
    }

    private void buildUI () {
        // Создается окно в стиле UTILITY без возможности изменять размер
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setResizable(false);

        // Указывается заголовок окна в зависимости от режима работы
        if (isEditMode) {
            stage.setTitle("Редактирование книги");
        } else {
            stage.setTitle("Создание новой записи");
        }

        // Инициализация окна
        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("ru/itlc/testproject/clientside/stylesheet.css");


        // !!! (НАЧАЛО) СОЗДАНИЕ ЭЛЕМЕНТОВ ФОРМЫ !!!
        // Создание строки для поля ввода имени автора
        Label lblBookAuthor = new Label("Автор: ");
        lblBookAuthor.setFocusTraversable(true);

        // Создание строки для поля ввода названия книги
        Label lblBookTitle = new Label("Название: ");

        // Создание строки для поля ввода названия издательства
        Label lblBookPublisher = new Label("Издательство: ");

        // Создание строки для поля ввода адреса издательства
        Label lblBookPublisherAddress = new Label("Адр. издат.: ");

        // Создание строки для поля ввода даты публикации книги
        Label lblBookPublishingDate = new Label("Дата публикации: ");

        // Создание текстового поля для ввода имени автора
        TextField txtBookAuthor = new TextField();

        // Создание текстового поля для ввода названия книги
        TextField txtBookTitle = new TextField();

        // Создание текстового поля для ввода названия издательства
        TextField txtBookPublisher = new TextField();

        // Создание текстового поля для ввода адреса издательства
        TextField txtBookPublisherAddress = new TextField();

        // Создание поля для выбора даты публикации книги
        DatePicker bookPublishingDate = new DatePicker();

        // Инициализация отображения в текстовых полях
        if (isEditMode) {
            // Отображение информации редактируемого объекта в полях ввода
            txtBookAuthor.setText(book.getBookAuthor());
            txtBookTitle.setText(book.getBookTitle());
            txtBookPublisher.setText(book.getBookPublisher());
            txtBookPublisherAddress.setText(book.getBookPublisherAddress());
            bookPublishingDate.setValue(LocalDate.parse(book.getBookPublishingDate()));
        } else {
            // Отображение поясняющих надписей в текстовых полях
            txtBookAuthor.setPromptText("Введите имя автора");
            txtBookTitle.setPromptText("Введите название книги");
            txtBookPublisher.setPromptText("Введите название издательства");
            txtBookPublisherAddress.setPromptText("Введите адрес издательства");
            bookPublishingDate.setPromptText("Введите дату публикации");
        }

        // Отображение красной звездочки при ошибке в поле ввода имени автора книги
        Label lblBookAuthorError = new Label("*");
        lblBookAuthorError.setVisible(false);
        lblBookAuthorError.getStyleClass().add("lbl-error");

        // Отображение красной звездочки при ошибке в поле ввода названия книги
        Label lblBookTitleError = new Label("*");
        lblBookTitleError.setVisible(false);
        lblBookTitleError.getStyleClass().add("lbl-error");

        // Отображение красной звездочки при ошибке в поле ввода имени издательства
        Label lblBookPublisherError = new Label("*");
        lblBookPublisherError.setVisible(false);
        lblBookPublisherError.getStyleClass().add("lbl-error");

        // Отображение красной звездочки при ошибке в поле ввода даты издания
        Label bookPublishingDateError = new Label("*");
        bookPublishingDateError.setVisible(false);
        bookPublishingDateError.getStyleClass().add("lbl-error");

        // Отображение сообщения об ошибки внизу формы
        Label lblErrorMessage = new Label(TEXT_ERROR_MESSAGE);
        lblErrorMessage.setVisible(false);
        lblErrorMessage.getStyleClass().add("lbl-error");

        // Создание кнопки "Сохранить"
        Button btnSave = new Button("Сохранить");

        // Создание обработчика нажатия на кнопку "Сохранить"
        btnSave.setOnAction((event -> {
            // Признак наличия ошибок
            boolean isProblemFound = false;

            // Проверка валидности данных введенных в поле автора книги (не пустое значение)
            if (txtBookAuthor.getText().isEmpty()) {
                lblBookAuthorError.setVisible(true);
                isProblemFound = true;
            }

            // Проверка валидности данных введенных в поле названия книги (не пустое значение)
            if (txtBookTitle.getText().isEmpty()) {
                lblBookTitleError.setVisible(true);
                isProblemFound = true;
            }

            // Проверка валидности данных введенных в поле названия издательства (не пустое значение)
            if (txtBookPublisher.getText().isEmpty()) {
                lblBookPublisherError.setVisible(true);
                isProblemFound = true;
            }

            //  Проверка валидности данных введенных в поле дата публикации (дата не является больше текущей)
            if (bookPublishingDate.getValue().compareTo(LocalDateTime.now().toLocalDate()) > 0) {
                lblErrorMessage.setText(lblErrorMessage.getText() + DATE_ERROR_MESSAGE);
                bookPublishingDateError.setVisible(true);
                isProblemFound = true;
            }

            // Если проблема найдена
            if (isProblemFound) {
                // то, выводим сообщение об ошибках
                lblErrorMessage.setVisible(true);
            } else {
                if (!isEditMode) {
                    // Создание новой записи в базе данных
                    Book bookForCreating = new Book(0,
                            txtBookAuthor.getText(),
                            txtBookTitle.getText(),
                            txtBookPublisher.getText(),
                            txtBookPublisherAddress.getText(),
                            bookPublishingDate.getValue().toString());
                    Book createdBook = HttpWorkUtils.saveBook(bookForCreating);
                    if (createdBook != null) {
                        stage.close();
                    }
                } else {
                    // Применение изменения для конкретной записи в базе данных
                    Book bookForUpdating = new Book(0,
                            txtBookAuthor.getText(),
                            txtBookTitle.getText(),
                            txtBookPublisher.getText(),
                            txtBookPublisherAddress.getText(),
                            bookPublishingDate.getValue().toString());
                    BooleanResponse result = HttpWorkUtils.updateBookById(book.getBookId(), bookForUpdating);
                    if (result != null && result.isStatus()) {
                        stage.close();
                    }
                }
            }
        }));

        // Создание кнопки "Отмена"
        Button btnCancel = new Button("Отмена");

        // Создание обработчика нажатия на кнопку "Отмена"
        btnCancel.setOnAction(event -> stage.close());

        // Создание панели кнопок и помещение в нее всех созданных кнопок
        ButtonBar btnBar = new ButtonBar();
        btnBar.getButtons().addAll(btnSave, btnCancel);
        // !!! (КОНЕЦ) СОЗДАНИЕ ЭЛЕМЕНТОВ ФОРМЫ !!!


        // !!! (НАЧАЛО) НАСТРОЙКА ОТОБРАЖЕНИЯ !!!
        // Добавление строки на панели для ввода/редактирования имени автора книги
        grid.add(lblBookAuthor, 0, 0, 1, 1);
        grid.add(txtBookAuthor, 1, 0, 1, 1);
        grid.add(lblBookAuthorError, 2, 0, 1, 1);

        // Добавление строки на панели для ввода/редактирования названия книги
        grid.add(lblBookTitle, 0, 1, 1, 1);
        grid.add(txtBookTitle, 1, 1, 1, 1);
        grid.add(lblBookTitleError,2, 1, 1, 1);

        // Добавление строки на панели для ввода/редактирования названия издательства
        grid.add(lblBookPublisher, 0, 2, 1, 1);
        grid.add(txtBookPublisher, 1, 2, 1, 1);
        grid.add(lblBookPublisherError, 2, 2, 1, 1);

        // Добавление строки на панели для ввода/редактирования адреса издательства
        grid.add(lblBookPublisherAddress, 0, 3, 1, 1);
        grid.add(txtBookPublisherAddress, 1, 3, 1, 1);

        // Добавление строки на панели для ввода/редактирования даты публикации
        grid.add(lblBookPublishingDate, 0, 4, 1, 1);
        grid.add(bookPublishingDate, 1, 4, 1, 1);
        grid.add(bookPublishingDateError, 2, 4, 1, 1);

        // Добавление строки с сообщением об ошибке
        grid.add(lblErrorMessage, 0, 5, 2, 1);

        // Добавление панели кнопок
        grid.add(btnBar, 0, 6, 2, 1);

        grid.setHgap(10);
        grid.setVgap(5);

        grid.setPadding(new Insets(10, 10, 20, 10));

        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();

        grid.getColumnConstraints().add(column1);
        grid.getColumnConstraints().add(column2);
        grid.getColumnConstraints().add(column3);

        column1.setPercentWidth(15);
        column2.setPercentWidth(83);
        column3.setPercentWidth(2);

        GridPane.setHalignment(lblBookAuthor, HPos.RIGHT);
        GridPane.setHalignment(lblBookTitle, HPos.RIGHT);
        GridPane.setHalignment(lblBookPublisher, HPos.RIGHT);
        GridPane.setHalignment(lblBookPublisherAddress, HPos.RIGHT);
        GridPane.setHalignment(lblBookPublishingDate, HPos.RIGHT);
        // !!! (КОНЕЦ) НАСТРОЙКА ОТОБРАЖЕНИЯ !!!

        root.setCenter(grid);
        stage.setScene(scene);
        stage.showAndWait();
    }
}

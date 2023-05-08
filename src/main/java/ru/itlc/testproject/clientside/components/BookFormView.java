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
        // Создаем окна в стиле UTILITY без возможности изменять размер
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setResizable(false);

        if (isEditMode) {
            stage.setTitle("Редактирование книги");
        } else {
            stage.setTitle("Создание новой записи");
        }

        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("ru/itlc/testproject/clientside/stylesheet.css");

        Label lblBookAuthor = new Label("Автор: ");
        lblBookAuthor.setFocusTraversable(true);
        Label lblBookTitle = new Label("Название: ");
        Label lblBookPublisher = new Label("Издательство: ");
        Label lblBookPublisherAddress = new Label("Адр. издат.: ");
        Label lblBookPublishingDate = new Label("Дата публикации: ");

        TextField txtBookAuthor = new TextField();
        TextField txtBookTitle = new TextField();
        TextField txtBookPublisher = new TextField();
        TextField txtBookPublisherAddress = new TextField();
        DatePicker bookPublishingDate = new DatePicker();
        if (isEditMode) {
            txtBookAuthor.setText(book.getBookAuthor());
            txtBookTitle.setText(book.getBookTitle());
            txtBookPublisher.setText(book.getBookPublisher());
            txtBookPublisherAddress.setText(book.getBookPublisherAddress());
            bookPublishingDate.setValue(LocalDate.parse(book.getBookPublishingDate()));
        } else {
            txtBookAuthor.setPromptText("Введите имя автора");
            txtBookTitle.setPromptText("Введите название книги");
            txtBookPublisher.setPromptText("Введите название издательства");
            txtBookPublisherAddress.setPromptText("Введите адрес издательства");
            bookPublishingDate.setPromptText("Введите дату публикации");
        }

        Label lblBookAuthorError = new Label("*");
        lblBookAuthorError.setVisible(false);
        lblBookAuthorError.getStyleClass().add("lbl-error");

        Label lblBookTitleError = new Label("*");
        lblBookTitleError.setVisible(false);
        lblBookTitleError.getStyleClass().add("lbl-error");

        Label lblBookPublisherError = new Label("*");
        lblBookPublisherError.setVisible(false);
        lblBookPublisherError.getStyleClass().add("lbl-error");

        Label bookPublishingDateError = new Label("*");
        bookPublishingDateError.setVisible(false);
        bookPublishingDateError.getStyleClass().add("lbl-error");

        Label lblErrorMessage = new Label(TEXT_ERROR_MESSAGE);
        lblErrorMessage.setVisible(false);
        lblErrorMessage.getStyleClass().add("lbl-error");

        Button btnSave = new Button("Сохранить");
        btnSave.setOnAction((event -> {
            if (!isEditMode) {
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
        }));

        Button btnCancel = new Button("Отмена");
        btnCancel.setOnAction(event -> stage.close());

        ButtonBar btnBar = new ButtonBar();
        btnBar.getButtons().addAll(btnSave, btnCancel);

        grid.add(lblBookAuthor, 0, 0, 1, 1);
        grid.add(txtBookAuthor, 1, 0, 1, 1);
        grid.add(lblBookAuthorError, 2, 0, 1, 1);

        grid.add(lblBookTitle, 0, 1, 1, 1);
        grid.add(txtBookTitle, 1, 1, 1, 1);
        grid.add(lblBookTitleError,2, 1, 1, 1);

        grid.add(lblBookPublisher, 0, 2, 1, 1);
        grid.add(txtBookPublisher, 1, 2, 1, 1);
        grid.add(lblBookPublisherError, 2, 2, 1, 1);

        grid.add(lblBookPublisherAddress, 0, 3, 1, 1);
        grid.add(txtBookPublisherAddress, 1, 3, 1, 1);

        grid.add(lblBookPublishingDate, 0, 4, 1, 1);
        grid.add(bookPublishingDate, 1, 4, 1, 1);
        grid.add(bookPublishingDateError, 2, 4, 1, 1);

        grid.add(lblErrorMessage, 0, 5, 2, 1);

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

        root.setCenter(grid);
        stage.setScene(scene);
        stage.showAndWait();
    }
}

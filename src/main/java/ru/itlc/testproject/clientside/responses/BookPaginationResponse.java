package ru.itlc.testproject.clientside.responses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class BookPaginationResponse {
    private final List<Book> books;
    private final int bookCount;
    private final int pageNumber;
    private final int totalPagesCount;
    private final int totalBooksCount;
}

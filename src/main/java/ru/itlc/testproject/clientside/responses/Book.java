package ru.itlc.testproject.clientside.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book implements Serializable {
    private long bookId;
    private String bookAuthor;
    private String bookTitle;
    private String bookPublisher;
    private String bookPublisherAddress;
    private String bookPublishingDate;
}

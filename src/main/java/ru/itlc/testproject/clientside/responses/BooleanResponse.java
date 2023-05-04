package ru.itlc.testproject.clientside.responses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BooleanResponse {
    private final boolean status;
}

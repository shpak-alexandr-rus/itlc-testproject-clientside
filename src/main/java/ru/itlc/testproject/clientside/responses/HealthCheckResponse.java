package ru.itlc.testproject.clientside.responses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class HealthCheckResponse {
    private final boolean dbStatus;

    @Override
    public String toString() {
        return "dbStatus: " + dbStatus;
    }
}

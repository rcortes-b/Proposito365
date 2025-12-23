package com.proposito365.app.dto;

public enum StatusEnum {
	IN_PROGRESS("In progress"),
    COMPLETED("Completed"),
	FAILED("Failed");

    private final String dbValue;

    StatusEnum(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
}

package com.proposito365.app.domain;

public enum RolesEnum {
	MEMBER("member"),
    ADMIN("admin");

    private final String dbValue;

    RolesEnum(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
}

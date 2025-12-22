package com.proposito365.app.domain;

/* This must be modified at the same time as the Status entity */

public enum StatusEnum {
	IN_PROGRESS(1L),
	COMPLETED(2L), 
	FAILED(3L);

	private final Long id;

	StatusEnum(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}

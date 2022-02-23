package com.hust.khanhkelvin.dto.response;

import lombok.Data;
import java.io.Serializable;
import java.time.Instant;

@Data
public abstract class AbstractAuditing implements Serializable {

	/** The Constant serialVersionUID */
	private static final long serialVersionUID = 2672912996730315557L;

	private String createdBy;

	private Instant createdDate;

	private String lastModifiedBy;

	private Instant lastModifiedDate;
}

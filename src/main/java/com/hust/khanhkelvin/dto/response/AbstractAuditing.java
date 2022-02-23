package com.hust.khanhkelvin.dto.response;

import lombok.Data;
import java.io.Serializable;

@Data
public abstract class AbstractAuditing implements Serializable {

	/** The Constant serialVersionUID */
	private static final long serialVersionUID = 2672912996730315557L;

	private String createdDate;

	private String lastModifiedDate;
}

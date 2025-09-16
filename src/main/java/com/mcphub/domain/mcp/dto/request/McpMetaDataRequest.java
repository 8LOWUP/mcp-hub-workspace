package com.mcphub.domain.mcp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpMetaDataRequest {

	private String name;
	private String version;
	private String description;
	private String imageUrl;
	private Boolean isKeyRequired;
	private Long categoryId;
	private Long platformId;
	private Long licenseId;
	private String sourceUrl;
	private List<McpToolRequest> tools;
}

package com.mcphub.domain.mcp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyUploadMcpRequest {
	private int page = 0;
	private int size = 12;
	private String search;
	private String sort = "latest";
}

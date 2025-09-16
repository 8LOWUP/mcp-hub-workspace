package com.mcphub.domain.mcp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpListRequest {
	private int page = 0;
	//TODO : FE 요구사항 확인 필요
	private int size = 12;
	private String search;
	private String category;
	private String sort;  // "latest" or "popular" or "rating"
}

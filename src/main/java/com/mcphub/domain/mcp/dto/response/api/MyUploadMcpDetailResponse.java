package com.mcphub.domain.mcp.dto.response.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyUploadMcpDetailResponse {
	private Long id;
	private String name;
	private String version;
	private String description;
	private String imageUrl;
	private String sourceUrl;
	private Boolean isKeyRequired;

	//현재 MCP가 선택한 값
	private Long categoryId;
	private String categoryName;

	private Long platformId;
	private String platformName;

	private Long licenseId;
	private String licenseName;

	// 상태값
	private Boolean isPublished;

	//최초 배포일
	private LocalDateTime publishedAt;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// 툴 설명
	private List<McpToolResponse> tools;
}

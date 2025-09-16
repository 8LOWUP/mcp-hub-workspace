package com.mcphub.domain.mcp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpUrlRequest {

	private String url;   // MCP 서버 URL
}

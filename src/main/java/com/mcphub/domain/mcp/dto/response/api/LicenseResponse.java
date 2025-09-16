package com.mcphub.domain.mcp.dto.response.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseResponse {
	private Long id;
	private String name;
}

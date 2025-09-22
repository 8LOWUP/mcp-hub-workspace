package com.mcphub.domain.workspace.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class McpSaveEvent {
	private Long userId;
	private Long mcpId;
}

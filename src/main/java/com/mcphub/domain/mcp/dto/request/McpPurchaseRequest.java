package com.mcphub.domain.mcp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpPurchaseRequest {
	//TODO 우선 무료 -> 추후 요금제 생길 수정
	 private String paymentMethod;
}

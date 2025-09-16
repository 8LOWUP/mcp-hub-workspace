package com.mcphub.domain.mcp.entity;

import com.mcphub.global.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mcp_metrics")
public class McpMetrics extends BaseEntity {

	@Id
	@Column(name = "mcp_id")
	private Long mcpId;
	//TODO : Metrix 테이블 추후 고려 ( 리뷰 수 평점 관리에 대한 테이블)
	// @OneToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "mcp_id", insertable = false, updatable = false) // 읽을 때
	// private Mcp mcp;
	//
	// @Column(name = "review_count", nullable = false)
	// private Integer reviewCount;
	//
	// @Column(name = "avg_rating", nullable = false)
	// private Float avgRating;
	//
	// @Column(name = "saved_user_count", nullable = false)
	// private Integer savedUserCount;
}

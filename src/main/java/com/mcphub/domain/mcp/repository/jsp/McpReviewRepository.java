package com.mcphub.domain.mcp.repository.jsp;

import com.mcphub.domain.mcp.entity.McpReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface McpReviewRepository extends JpaRepository<McpReview, Long> {
	@Query("SELECT COALESCE(AVG(r.rating), 0) FROM McpReview r WHERE r.mcp.id = :mcpId")
	Float getAverageRating(@Param("mcpId") Long mcpId);

	@Query("SELECT COUNT(r) FROM McpReview r WHERE r.mcp.id = :mcpId")
	Integer getReviewCount(@Param("mcpId") Long mcpId);

}

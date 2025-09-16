package com.mcphub.domain.mcp.repository.jsp;

import com.mcphub.domain.mcp.entity.ArticleMcpTool;
import com.mcphub.domain.mcp.entity.Mcp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMcpToolRepository extends JpaRepository<ArticleMcpTool, Long> {
	void deleteByMcp(Mcp mcp);
}

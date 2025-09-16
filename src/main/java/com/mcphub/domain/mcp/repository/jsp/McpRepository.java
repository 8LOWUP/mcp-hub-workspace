package com.mcphub.domain.mcp.repository.jsp;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mcphub.domain.mcp.entity.Mcp;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface McpRepository extends JpaRepository<Mcp, Long> {
	Optional<Mcp> findByIdAndDeletedAtIsNull(Long id);
}

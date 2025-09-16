package com.mcphub.domain.mcp.repository.jsp;

import com.mcphub.domain.mcp.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
}

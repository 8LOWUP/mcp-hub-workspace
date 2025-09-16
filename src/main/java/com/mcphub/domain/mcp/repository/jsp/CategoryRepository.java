package com.mcphub.domain.mcp.repository.jsp;

import com.mcphub.domain.mcp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

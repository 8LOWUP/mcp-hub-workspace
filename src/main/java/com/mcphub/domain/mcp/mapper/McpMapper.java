package com.mcphub.domain.mcp.mapper;

import org.springframework.stereotype.Component;
import com.mcphub.domain.mcp.dto.response.readmodel.TestReadDto;
import com.mcphub.domain.mcp.entity.Mcp;

@Component
public class McpMapper {

	 public TestReadDto testMapper(Mcp mcp){
        return TestReadDto.builder()
            .id(mcp.getId())
            .build();
	 }
}

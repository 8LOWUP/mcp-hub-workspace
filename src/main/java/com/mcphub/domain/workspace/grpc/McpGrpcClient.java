package com.mcphub.domain.workspace.grpc;

import com.mcphub.domain.mcp.grpc.McpServiceGrpc;
import com.mcphub.domain.mcp.grpc.McpUrlTokenRequest;
import com.mcphub.domain.mcp.grpc.McpUrlTokenResponse;
import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class McpGrpcClient {

    @GrpcClient("mcpService")
    private McpServiceGrpc.McpServiceBlockingStub mcpStub;

    public List<McpUrlTokenPair> getMcpUrlTokenPairs(String userId, List<Long> mcpIds) {
        McpUrlTokenRequest request = McpUrlTokenRequest.newBuilder()
                .setUserId(userId)
                .addAllMcpIds(mcpIds)
                .build();

        McpUrlTokenResponse response = mcpStub.getMcpUrlTokenPairs(request);

        // gRPC → 내부 DTO 변환
        return response.getPairsList().stream()
                .map(pair -> new com.mcphub.domain.workspace.dto.McpUrlTokenPair(
                        pair.getUrl(),
                        pair.getToken()
                ))
                .toList();
    }
}


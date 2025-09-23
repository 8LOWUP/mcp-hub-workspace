package com.mcphub.domain.workspace.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcphub.domain.workspace.adviser.UserMcpAdviser;
import com.mcphub.domain.workspace.dto.event.McpSaveEvent;
import com.mcphub.domain.workspace.dto.event.UrlSaveEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class McpConsumer {

    private final StringRedisTemplate redisTemplate;
    private final UserMcpAdviser userMcpAdviser;
    private static final String MESSAGE_KEY_PREFIX = "kafka:msg:";

    private boolean isDuplicate(String messageId) {
        if (messageId == null) return false;
        Boolean exists = redisTemplate.hasKey(MESSAGE_KEY_PREFIX + messageId);
        if (Boolean.TRUE.equals(exists)) {
            log.info("Duplicate message ignored: {}", messageId);
            return true;
        }
        redisTemplate.opsForValue().set(MESSAGE_KEY_PREFIX + messageId, "processed", Duration.ofHours(1));
        return false;
    }

    private String extractMessageId(ConsumerRecord<String, String> record) {
        if (record.headers().lastHeader("messageId") != null) {
            return new String(record.headers().lastHeader("messageId").value());
        }
        return null;
    }

    // 유저가 MCP 저장
    @KafkaListener(topics = "user-saved-mcp")
    public void consumeUserSavedMcp(ConsumerRecord<String, String> record) {
        String messageId = extractMessageId(record);
        if (isDuplicate(messageId)) return;

        // McpSaveEvent로 파싱
        try {
            McpSaveEvent event = new ObjectMapper().readValue(record.value(), McpSaveEvent.class);
            log.info("Processing user-saved-mcp: {}", event.toString());
            userMcpAdviser.createUserMcp(event);
        } catch (Exception e) {
            log.error("Failed to process user-saved-mcp", e);
        }
    }

    // 유저가 MCP 삭제
    @KafkaListener(topics = "user-deleted-mcp")
    public void consumeUserDeletedMcp(ConsumerRecord<String, String> record) {
        String messageId = extractMessageId(record);
        if (isDuplicate(messageId)) return;

        try {
            McpSaveEvent event = new ObjectMapper().readValue(record.value(), McpSaveEvent.class);
            log.info("Processing user-deleted-mcp: {}", event.toString());
            userMcpAdviser.deleteUserMcp(event);
        } catch (Exception e) {
            log.error("Failed to process user-deleted-mcp", e);
        }
    }

    // 1. MCP URL 변경 시
	// 2. MCP가 배포 상태로 전환 시 (미배포 -> 배포)
    @KafkaListener(topics = "mcp-saved-url")
    public void consumeMcpSavedUrl(ConsumerRecord<String, String> record) {
        String messageId = extractMessageId(record);
        if (isDuplicate(messageId)) return;

        try {
            UrlSaveEvent event = new ObjectMapper().readValue(record.value(), UrlSaveEvent.class);
            log.info("Processing mcp-saved-url: {}", event.toString());
            userMcpAdviser.createAndUpdateMcpUrl(event);
        } catch (Exception e) {
            log.error("Failed to process mcp-saved-url", e);
        }
    }

    // 1. MCP 배포 상태가 미배포 상태로 변환
    // 2. MCP 가 삭제된 경우
    @KafkaListener(topics = "mcp-deleted-url")
    public void consumeMcpDeletedUrl(ConsumerRecord<String, String> record) {
        String messageId = extractMessageId(record);
        if (isDuplicate(messageId)) return;

        try {
            UrlSaveEvent event = new ObjectMapper().readValue(record.value(), UrlSaveEvent.class);
            log.info("Processing mcp-deleted-url: {}", event.toString());
            userMcpAdviser.deleteMcpUrl(event);
        } catch (Exception e) {
            log.error("Failed to process mcp-deleted-url", e);
        }
    }
}


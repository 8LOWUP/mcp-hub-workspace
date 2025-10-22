package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.dto.request.CreateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.request.UpdateLlmTokenCommand;
import com.mcphub.domain.workspace.entity.LlmToken;
import com.mcphub.domain.workspace.entity.enums.Llm;
import com.mcphub.domain.workspace.repository.mongo.LlmTokenMongoRepository;
import com.mcphub.domain.workspace.status.LlmErrorStatus;
import com.mcphub.domain.workspace.status.WorkspaceErrorStatus;
import com.mcphub.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LlmTokenServiceImpl implements LlmTokenService {
    private final LlmTokenMongoRepository llmTokenMongoRepository;
    private final StringEncryptor stringEncryptor;

    @Transactional
    public List<LlmToken> getAll(String userId) {
        List<LlmToken> llmTokenList = llmTokenMongoRepository.findByUserId(userId);

        return llmTokenList.stream()
                .peek(llmToken -> {
                    String decryptedToken = stringEncryptor.decrypt(llmToken.getToken());
                    llmToken.setToken(decryptedToken);
                })
                .toList();
    }

    // @Transactional
    // public LlmToken get(String userId, Llm llmId) {
    //     return llmTokenMongoRepository.findByUserIdAndLlmId(userId, llmId)
    //             .map(token -> {
    //                 token.setToken(stringEncryptor.decrypt(token.getToken()));
    //                 return token;
    //             })
    //             .orElse(LlmToken.builder()
    //                     .llmId(llmId)
    //                     .token(null)
    //                     .build());
    // }

    @Transactional(readOnly = true)
    public LlmToken get(String userId, Llm llmId) {
        return llmTokenMongoRepository.findByUserIdAndLlmId(userId, llmId)
            .map(src -> {
                String value = tryDecryptOrReturnRaw(src.getToken());
                return LlmToken.builder()
                    .id(src.getId())
                    .userId(src.getUserId())
                    .llmId(src.getLlmId())
                    .token(value)               // ← 조회 응답에 토큰 “그대로” 넣음
                    .build();
            })
            .orElse(LlmToken.builder()
                .llmId(llmId)
                .token(null)
                .build());
    }


    @Transactional
    public LlmToken create(CreateLlmTokenCommand cmd) {
        //유저가 과거에 등록한적 있는지 확인
        if (llmTokenMongoRepository.existsByUserIdAndLlmId(cmd.userId(), cmd.llmId()))
            throw new RestApiException(LlmErrorStatus.TOKEN_ALREADY_EXISTS);

        String encryptedToken = stringEncryptor.encrypt(cmd.llmToken());

        return llmTokenMongoRepository.save(LlmToken.builder()
                        .userId(cmd.userId())
                        .llmId(cmd.llmId())
                        .token(encryptedToken)
                        .build());
    }

    @Transactional
    public LlmToken update(UpdateLlmTokenCommand cmd) {
        LlmToken llmToken = llmTokenMongoRepository.findByUserIdAndLlmId(cmd.userId(), cmd.llmId())
                .orElseThrow(() -> new RestApiException(LlmErrorStatus.TOKEN_NOT_EXISTS));

        String encryptedToken = stringEncryptor.encrypt(cmd.llmToken());
        llmToken.setToken(encryptedToken);

        return llmToken;
    }

    private String tryDecryptOrReturnRaw(String token) {
        if (token == null || token.isBlank()) return null;

        // 1) 이미 평문 규격이면(예: sk- 시작) 그대로 반환
        if (token.startsWith("sk-")) return token;

        // 2) ENC(...)처럼 암호문 포맷이면 복호화 시도하되 실패 시 “그대로” 반환
        try {
            // 암호화가 설정된 환경이면 복호화 성공, 아니면 예외 발생 → catch에서 원문 반환
            return stringEncryptor.decrypt(token);
        } catch (Throwable t) {
            // log.warn("LLM token decrypt failed, return raw as-is", t);
            return token; // ← 포인트: 절대 null로 만들지 말고 저장된 문자열 그대로
        }
    }
}

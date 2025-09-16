package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.dto.request.CreateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.request.UpdateLlmTokenCommand;
import com.mcphub.domain.workspace.entity.LlmToken;
import com.mcphub.domain.workspace.repository.mongo.LlmTokenMongoRepository;
import com.mcphub.domain.workspace.status.LlmErrorStatus;
import com.mcphub.global.common.exception.RestApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LlmTokenServiceImpl implements LlmTokenService {
    private final LlmTokenMongoRepository llmTokenMongoRepository;
    private final StringEncryptor stringEncryptor;

    @Transactional
    public List<LlmToken> get(String userId) {
        List<LlmToken> llmTokenList = llmTokenMongoRepository.findByUserId(userId);

        return llmTokenList.stream()
                .peek(llmToken -> {
                    String decryptedToken = stringEncryptor.decrypt(llmToken.getToken());
                    llmToken.setToken(decryptedToken);
                })
                .toList();
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
        //유저가 등록한 기록이 있는지 확인
        if(!llmTokenMongoRepository.existsByUserIdAndLlmId(cmd.userId(), cmd.llmId()))
            throw new RestApiException(LlmErrorStatus.TOKEN_NOT_EXISTS);

        //DB 업데이트
        LlmToken llmToken = llmTokenMongoRepository.findByUserIdAndLlmId(cmd.userId(), cmd.llmId());
        String encryptedToken = stringEncryptor.encrypt(cmd.llmToken());
        llmToken.setToken(encryptedToken);

        return llmToken;
    }
}

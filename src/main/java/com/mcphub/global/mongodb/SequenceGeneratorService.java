package com.mcphub.global.mongodb;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {

    private final MongoTemplate mongoTemplate;

    // MongoDB의 autoincrement 기능
    // 사용 방법 예시 (Message 라는 엔티티 클래스가 있다고 가정)
    // private final SequenceGeneratorService sequenceGeneratorService;
    // public Message saveMessage(Message message) {
    //        message.setId(sequenceGeneratorService.getNextSequence(Message.SEQUENCE_NAME));
    //        return messageRepository.save(message);
    //    }
    public Long getNextSequence(String seqName) {
        Counter counter = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Counter.class
        );
        return counter.getSeq();
    }
}

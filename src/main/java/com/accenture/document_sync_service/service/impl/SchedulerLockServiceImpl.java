package com.accenture.document_sync_service.service.impl;

import java.time.Duration;
import java.time.Instant;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.entity.SchedulerLock;

import com.accenture.document_sync_service.service.SchedulerLockService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulerLockServiceImpl
        implements SchedulerLockService {
private static final Duration LOCK_TIMEOUT = Duration.ofMinutes(30);
    private static final String LOCK_ID = "document-sync-lock";

    private final MongoTemplate mongoTemplate;

    @Override
    public boolean acquireLock() {

        Instant now = Instant.now();
        Instant staleLockTime = now.minus(LOCK_TIMEOUT);
        Query query = new Query(
        new Criteria().andOperator(
                Criteria.where("_id").is(LOCK_ID),
                new Criteria().orOperator(
                        Criteria.where("lockedAt").is(null),
                        Criteria.where("lockedAt").lt(staleLockTime))));;

        Update update = new Update()
                .set("lockedAt", now);

        FindAndModifyOptions options = new FindAndModifyOptions()
                .upsert(true)
                .returnNew(true);

        SchedulerLock lock = mongoTemplate.findAndModify(
                query,
                update,
                options,
                SchedulerLock.class);

        return lock != null;
    }

    @Override
public void releaseLock() {

    Query query = new Query(
            Criteria.where("_id").is(LOCK_ID));

    Update update = new Update()
            .unset("lockedAt");

    mongoTemplate.updateFirst(
            query,
            update,
            SchedulerLock.class);
}
}
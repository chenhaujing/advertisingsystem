package com.sztouyun.advertisingsystem.repository.job;


import com.sztouyun.advertisingsystem.model.job.SyncStoreLog;
import com.sztouyun.advertisingsystem.repository.BaseRepository;

public interface SyncStoreLogRepository extends BaseRepository<SyncStoreLog> {
    SyncStoreLog findFirstByIsSuccessOrderByCreatedTimeDesc(int isSuccess);
}
package com.sztouyun.advertisingsystem.repository.contract;

import com.sztouyun.advertisingsystem.model.contract.ContractStore;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractStoreRepository extends BaseRepository<ContractStore> {

    List<ContractStore> findByContractId(String contractId);

    int countByContractIdAndStoreType(String contractId, int storeType);

    void deleteByContractIdAndStoreType(String contractId, int storeType);

    @Query(value = "SELECT id from ContractStore where contractId = :contractId And storeId = :storeId")
    String findContractStoreId(@Param("contractId") String contractId, @Param("storeId") String storeId);

    void deleteByContractIdAndStoreId(String contractId, String storeId);

    void deleteByContractId(String contractId);
}
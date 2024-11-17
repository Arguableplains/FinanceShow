package com.FS.FinanceShow_demo.repository;

import com.FS.FinanceShow_demo.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserId(Long userId);
    List<Transaction> findAllByUserIdAndAccountId(Long userId, Long accountId);
}

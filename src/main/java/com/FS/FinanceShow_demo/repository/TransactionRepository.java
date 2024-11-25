package com.FS.FinanceShow_demo.repository;

import com.FS.FinanceShow_demo.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserId(Long userId);
    List<Transaction> findAllByUserIdAndCategoryId(Long userId, Long categoryId);
    List<Transaction> findAllByUserIdAndAccountId(Long userId, Long accountId);
    List<Transaction> findAllByUserIdAndAccountIdAndCategoryId(Long userId, Long accountId, Long categoryId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId")
    Optional<Double> sumByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.account.id = :accountId")
    Optional<Double> sumByUserIdAndAccountId(@Param("userId") Long userId, @Param("accountId") Long accountId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.category.id = :categoryId")
    Optional<Double> sumByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.category.id = :categoryId AND t.account.id = :accountId")
    Optional<Double> sumByUserIdCategoryIdAndAccountId(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("accountId") Long accountId);

}

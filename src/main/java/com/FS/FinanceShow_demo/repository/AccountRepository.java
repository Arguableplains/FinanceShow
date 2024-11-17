package com.FS.FinanceShow_demo.repository;

import com.FS.FinanceShow_demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    public Optional<Account> findByName(String name);
    public List<Account> findByUser_Id(Long userId);
}

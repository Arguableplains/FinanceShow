package com.FS.FinanceShow_demo.services;

import com.FS.FinanceShow_demo.entity.Account;
import com.FS.FinanceShow_demo.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public void delete(Account account) {
        accountRepository.delete(account);
    }

    public List<Account> findByUserId(Long userId) {
        return accountRepository.findByUser_Id(userId);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        return optionalAccount.orElse(null); // Return null if not found
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    public Account findByName(String name) {
        Optional<Account> optionalAccount = accountRepository.findByName(name);
        return optionalAccount.orElse(null); // Return null if not found
    }
}

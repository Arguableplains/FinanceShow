package com.FS.FinanceShow_demo.services;

import com.FS.FinanceShow_demo.entity.Transaction;
import com.FS.FinanceShow_demo.entity.User;
import com.FS.FinanceShow_demo.repository.TransactionRepository;
import com.FS.FinanceShow_demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void delete(Transaction transaction){
        transactionRepository.delete(transaction);
    }
    
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction findById(Long id) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        return optionalTransaction.orElse(null);
    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<Transaction> findAllTransactionsForCurrentUser() {
        // Get the current authenticated user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        
        // Get the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        
        return transactionRepository.findAllByUserId(user.getId());
    }
}

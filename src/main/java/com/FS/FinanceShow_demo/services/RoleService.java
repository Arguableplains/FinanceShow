package com.FS.FinanceShow_demo.services;

import com.FS.FinanceShow_demo.entity.Role;
import com.FS.FinanceShow_demo.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void save(Role role) {
        roleRepository.save(role);
    }

    public void delete(Role role){
        roleRepository.delete(role);
    }

    public Role findById(Long id){
        return roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));
    }

    public Role findByName(String name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        return optionalRole.orElse(null); // Return null if not found
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}


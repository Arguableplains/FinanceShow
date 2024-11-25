package com.FS.FinanceShow_demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "category")
public class Category{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false)
    @NotBlank(message = "{entity.name.required}")
    @Size(min = 2, max = 50, message = "{entity.name.size}")
    private String name;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "USER_FOREING_KEY", nullable = false)
    @NotNull(message = "{entity.user.required}")
    private User user;

    public Category(){}

    public Category(Long id, String name, User user){
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public Category(String name, User user){
        this.name = name;
        this.user = user;
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}


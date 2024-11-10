package com.FS.FinanceShow_demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Column(name = "EMAIL", nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Column(name = "CELLPHONE", nullable = false, unique = true)
    @NotBlank(message = "Cellphone is required")
    @Pattern(regexp = "\\+?\\d{10,15}", message = "Cellphone should be a valid number with 10 to 15 digits, optionally prefixed by '+'")
    private String cellphone;

    @Column(name = "PICTURE")
    @Pattern(
    regexp = "^data:image/(jpeg|png|gif|bmp|webp);base64,[A-Za-z0-9+/=]+$",
    message = "Picture must be a valid base64-encoded image in JPEG, PNG, GIF, BMP, or WEBP format"
)
    private String picture;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @NotEmpty(message = "User must have at least one role")
    private Set<Role> roles = new HashSet<>();;

    // Constructor
    public User() {}

    public User(String name, String email, String password, String cellphone, String picture, Set<Role> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cellphone = cellphone;
        this.picture = picture;
        this.roles = roles;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<Role> getRoles() {

        if (roles == null) {
            roles = new HashSet<>();
        }

        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

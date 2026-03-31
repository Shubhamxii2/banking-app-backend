// package com.chh_shu.banking_app.Entity;

// import java.util.List;

// import com.chh_shu.banking_app.Models.Users.UserRole;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.Table;
// import lombok.Data;

// @Data
// @Entity
// @Table(name="users")
// public class UserEntity {

//     @Id //Primary Key
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name = "user_id")
//     private Long userId;

//     private String username;
//     private String password;

//     @Enumerated(EnumType.STRING)
//     @Column(name = "role", nullable = false)
//     private UserRole role;

//     @OneToMany(mappedBy = "user")
//     private List<AccountEntity> accounts;
// }

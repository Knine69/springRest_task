package com.jhuguet.sb_taskv1.app.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@DynamicUpdate
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String email;
    @Column(unique = true)
    private String password;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Order> orders;

    private String role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.orders = new HashSet<>();
    }

    public User(String username, String email, Set<Order> orders) {
        this.username = username;
        this.email = email;
        this.orders = orders;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password, Set<Order> orders, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.orders = orders;
        this.role = role;
    }

    public void placeOrder(Order order) {
        this.orders.add(order);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}

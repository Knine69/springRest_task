package com.jhuguet.sb_taskv1.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "placed_on")
    private Date timestamp;
    @OneToMany(mappedBy = "userOrder")
    private Set<GiftCertificate> certificates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    private BigDecimal cost;

    public Order(Date timestamp, Set<GiftCertificate> certificates) {
        this.timestamp = timestamp;
        this.certificates = certificates;
        calculateCost();
    }

    public Order(Date timestamp) {
        this.timestamp = timestamp;
        this.certificates = new HashSet<>();
        calculateCost();
    }

    public void addCertificate(GiftCertificate certificate) {
        this.certificates.add(certificate);
        calculateCost();
    }

    public void calculateCost() {
        this.cost = BigDecimal.ZERO;
        this.certificates.forEach(c -> this.cost = this.cost.add(new BigDecimal(c.getPrice().toString())));
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setCertificates(Set<GiftCertificate> certificates) {
        this.certificates = certificates;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}

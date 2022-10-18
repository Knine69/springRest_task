package com.jhuguet.sb_taskv1.app.models;

import lombok.Getter;

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
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date timestamp;
    @OneToMany(mappedBy = "userOrder")
    private Set<GiftCertificate> certificates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private BigDecimal cost;

    public Order(Date timestamp, Set<GiftCertificate> certificates) {
        this.timestamp = timestamp;
        this.certificates = certificates;
        this.cost = BigDecimal.ZERO;
    }

    public void addCertificate(GiftCertificate certificate) {
        this.certificates.add(certificate);
        calculateCost();
    }

    private void calculateCost() {
        this.cost = BigDecimal.ZERO;
        this.certificates.forEach(c -> this.cost.add(new BigDecimal(c.getPrice().toString())));
    }
}

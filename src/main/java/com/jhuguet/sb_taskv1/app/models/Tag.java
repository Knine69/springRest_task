package com.jhuguet.sb_taskv1.app.models;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "tag")
@DynamicUpdate
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "tag_name")
    private String name;
    @Column(name = "certificate_id")
    @ManyToMany(cascade = CascadeType.ALL)
    private List<GiftCertificate> certificates;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GiftCertificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<GiftCertificate> certificates) {
        this.certificates = certificates;
    }
}

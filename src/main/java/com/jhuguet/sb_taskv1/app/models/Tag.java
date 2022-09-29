package com.jhuguet.sb_taskv1.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Set;

@Entity
@Table(name = "tag")
@DynamicUpdate
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tag_name")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL,
            mappedBy = "associatedTags")
    @JsonIgnore
    private Set<GiftCertificate> certificates;

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String name, Set<GiftCertificate> certificates) {
        this.name = name;
        this.certificates = certificates;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GiftCertificate> getCertificates() {
        return certificates;
    }

}

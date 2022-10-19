package com.jhuguet.sb_taskv1.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "gift_certificate")
@DynamicUpdate
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "certificate_name")
    private String name;
    @Column(name = "certificate_description")
    private String description;
    private BigDecimal price;
    private int duration;
    @Column(name = "create_date")
    private String createDate;
    @Column(name = "last_update_date")
    private String lastUpdateDate;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "certificate_tags",
            joinColumns = {
                    @JoinColumn(name = "certificate_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", referencedColumnName = "id")
            })
    private Set<Tag> associatedTags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order userOrder;

    public GiftCertificate(String name, String description, BigDecimal price, int duration,
                           String createDate, String lastUpdateDate, Set<Tag> associatedTags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.associatedTags = associatedTags;
    }

    public GiftCertificate(int id, String name, String description, BigDecimal price, int duration,
                           String createDate, String lastUpdateDate, Set<Tag> associatedTags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.associatedTags = associatedTags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }


    public void assignTag(Tag tag) {
        this.associatedTags.add(tag);
    }

    public void cleanTags() {
        this.associatedTags.removeAll(getAssociatedTags());
    }

    public void placeOrder(GiftCertificate certificate) {
        userOrder.addCertificate(certificate);
    }


}

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
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "gift_certificate")
@DynamicUpdate
@NoArgsConstructor
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ManyToMany(cascade = CascadeType.ALL)
    @Column(name = "tag_id")
    private List<Tag> associatedTags;

    public int getId() {
        return id;
    }

    public List<Tag> getAssociatedTag() {
        return associatedTags;
    }

    public void setAssociatedTag(List<Tag> associatedTag) {
        this.associatedTags = associatedTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}

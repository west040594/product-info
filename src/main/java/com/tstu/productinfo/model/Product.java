package com.tstu.productinfo.model;

import com.tstu.commons.model.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Table(name = "tb_product")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    private Category category;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "average_price", nullable = true)
    private BigDecimal averagePrice;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @Column(name = "rating", nullable = true)
    private Double rating;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Set<ReviewSystemLink> reviewSystemLinks;

    @Column(name = "is_fill", nullable = true)
    private boolean isFill;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Set<Review> reviews;

    @Column(name = "created_by", nullable = true)
    private String createdBy;

    @Column(name = "last_modified_by", nullable = true)
    private String lastModifiedBy;

}

package com.tstu.productinfo.model;

import com.tstu.commons.model.BaseEntity;
import com.tstu.commons.model.converters.ReviewSystemConverter;
import com.tstu.commons.model.enums.ReviewSystem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;


@Table(name = "tb_review")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", nullable = true)
    private String body;

    @Column(name = "rating", nullable = true)
    private Double rating;

    @Column(name = "post_time", nullable = true)
    private Long postTime;

    @Column(name = "reviewer_name", nullable = true)
    private String reviewerName;

    @Column(name = "read_link", nullable = false)
    private String readLink;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "review_system", nullable = false)
    @Convert(converter = ReviewSystemConverter.class)
    private ReviewSystem reviewSystem;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "tb_review_quality",
            joinColumns = @JoinColumn(name = "review_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "quality_id", referencedColumnName = "id"))
    private Set<Quality> qualities;

}

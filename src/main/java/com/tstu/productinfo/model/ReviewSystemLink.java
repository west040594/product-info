package com.tstu.productinfo.model;

import com.tstu.commons.model.converters.ReviewSystemConverter;
import com.tstu.commons.model.enums.ReviewSystem;
import lombok.*;

import javax.persistence.*;

@Table(name = "tb_review_system_link")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSystemLink {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "serial")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "review_system", nullable = false)
    @Convert(converter = ReviewSystemConverter.class)
    private ReviewSystem reviewSystem;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")
    private Product product;

    public ReviewSystemLink(String name, ReviewSystem reviewSystem) {
        this.name = name;
        this.reviewSystem = reviewSystem;
    }
}

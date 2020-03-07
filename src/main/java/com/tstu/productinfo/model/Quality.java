package com.tstu.productinfo.model;

import com.tstu.commons.model.BaseEntity;
import com.tstu.commons.model.enums.QualityType;
import com.tstu.productinfo.model.converters.QualityTypeConverter;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Table(name = "tb_quality", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type"}))
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quality extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    @Convert(converter = QualityTypeConverter.class)
    private QualityType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Quality quality = (Quality) o;
        return name.equals(quality.name) &&
                type == quality.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, type);
    }

    //    @ManyToMany(mappedBy = "qualities", fetch = FetchType.LAZY)
//    private Set<Review> reviews;
}

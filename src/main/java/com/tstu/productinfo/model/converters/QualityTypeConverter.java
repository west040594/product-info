package com.tstu.productinfo.model.converters;

import com.tstu.commons.model.enums.QualityType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class QualityTypeConverter implements AttributeConverter<QualityType, String> {
    @Override
    public String convertToDatabaseColumn(QualityType qualityType) {
        if(qualityType == null) {
            return null;
        }
        return qualityType.getValue();
    }

    @Override
    public QualityType convertToEntityAttribute(String value) {
        if(value == null) {
            return null;
        }
        return QualityType.fromValue(value);
    }
}

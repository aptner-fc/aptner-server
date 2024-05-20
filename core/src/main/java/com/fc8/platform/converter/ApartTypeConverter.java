package com.fc8.platform.converter;


import com.fc8.platform.domain.enums.ApartType;
import jakarta.persistence.Converter;

@Converter
public class ApartTypeConverter extends CodeConverter<ApartType> {

    public ApartTypeConverter() {
        super(ApartType.class);
    }
}

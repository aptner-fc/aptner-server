package com.fc8.aptner.core.converter;

import com.fc8.aptner.core.domain.enums.TermsType;
import jakarta.persistence.Converter;

@Converter
public class TermsTypeConverter extends CodeConverter<TermsType> {

    public TermsTypeConverter() {
        super(TermsType.class);
    }
}

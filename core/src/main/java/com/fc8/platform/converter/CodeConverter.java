package com.fc8.platform.converter;

import com.fc8.platform.domain.enums.CodeEnum;
import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;

import java.util.EnumSet;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class CodeConverter<E extends Enum<E> & CodeEnum> implements AttributeConverter<E, String> {

    private final Class<E> target;

    @Override
    public String convertToDatabaseColumn(E codeEnum) {
        return codeEnum.getCode();
    }

    @Override
    public E convertToEntityAttribute(String code) {
        return EnumSet.allOf(target).stream()
                .filter(e -> e.getCode().equals(code))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

}

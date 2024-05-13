package com.fc8.aptner.core.domain.entity.apartment;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
public class Apartment {

    @Column(name = "apartment_dong", nullable = false, columnDefinition = "int comment '아파트 동'")
    private int dong;

    @Column(name = "apartment_ho", nullable = false, columnDefinition = "int comment '아파트 호'")
    private int ho;

    public static Apartment build(int dong, int ho) {
        return Apartment.builder()
                .dong(dong)
                .ho(ho)
                .build();
    }
}

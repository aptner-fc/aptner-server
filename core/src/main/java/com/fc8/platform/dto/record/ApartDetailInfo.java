package com.fc8.platform.dto.record;

import com.fc8.platform.domain.embedded.ApartDetail;
import jakarta.validation.constraints.NotNull;

public record ApartDetailInfo(@NotNull int dong, @NotNull int ho) {

    public ApartDetail toDomain() {
        return ApartDetail.build(dong, ho);
    }

}

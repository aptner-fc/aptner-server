package com.fc8.platform.domain;

import com.fc8.platform.domain.entity.apartment.Apart;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseApartEntity extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apart_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '아파트 정보'")
    private Apart apart;

    protected void updateApart(Apart apart) {
        this.apart = apart;
    }

}

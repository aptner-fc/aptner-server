package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.ApartAreaSummary;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadApartAreaResponse {

    private final List<ApartAreaSummary> apartAreaList;

    public LoadApartAreaResponse(List<ApartAreaSummary> apartAreaList) {
        this.apartAreaList = apartAreaList;
    }
}

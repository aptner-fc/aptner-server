package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.disclosure.DisclosureFile;

public record DisclosureFileInfo(
    Long id,
    String name,
    String path,
    Long size
) {
    public static DisclosureFileInfo fromEntity(DisclosureFile disclosureFile) {
        return new DisclosureFileInfo(
            disclosureFile.getId(),
            disclosureFile.getName(),
            disclosureFile.getPath(),
            disclosureFile.getSize()
        );
    }
}


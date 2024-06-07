package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.disclosure.DisclosureFile;

import java.util.List;

public interface DisclosureFileRepository {

    List<DisclosureFile> getDisclosureFileListByDisclosure(Disclosure disclosure);

}

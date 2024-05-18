package com.fc8.service;

import com.fc8.platform.dto.command.SavedTermsCommand;

public interface TermsService {

    Long register(SavedTermsCommand command);
}

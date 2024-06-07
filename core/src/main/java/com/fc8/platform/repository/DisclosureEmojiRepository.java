package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.disclosure.DisclosureEmoji;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;

public interface DisclosureEmojiRepository {
    EmojiCountInfo getEmojiCountInfoByDisclosureAndMember(Disclosure disclosure);

    EmojiReactionInfo getEmojiReactionInfoByDisclosureAndMember(Disclosure disclosure, Member member);

    boolean existsByDisclosureAndMemberAndEmoji(Disclosure disclosure, Member member, EmojiType emoji);

    DisclosureEmoji store(DisclosureEmoji disclosureEmoji);

    DisclosureEmoji getByDisclosureAndMemberAndEmoji(Disclosure disclosure, Member member, EmojiType emoji);

    void delete(DisclosureEmoji disclosureEmoji);
}


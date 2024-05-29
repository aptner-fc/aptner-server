package com.fc8.platform.dto.command;


import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.post.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WritePostCommand {

    private final String categoryCode;

    private final String title;

    private final String content;

    public Post toEntity(Category category, Member member, Apart apart) {
        return Post.create(category, member, apart, title, content);
    }

}

package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.repository.CategoryRepository;
import com.fc8.server.CategoryJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final CategoryJpaRepository categoryJpaRepository;

    QCategory category = QCategory.category;

    @Override
    public Category getChildCategoryByCode(String code) {
        Category childCategory = jpaQueryFactory
                .selectFrom(category)
                .where(
                        isUsed(),
                        eqCode(code),
                        isChildCategory()
                )
                .fetchOne();

        return Optional.ofNullable(childCategory)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_CATEGORY));
    }

    @Override
    public List<Category> getParentCategory() {
        return jpaQueryFactory
            .selectFrom(category)
            .where(
                isParentCategory(),
                isUsed()
            )
            .fetch();
    }

    private BooleanExpression isParentCategory() {
        return category.parent.isNull();
    }

    private BooleanExpression isChildCategory() {
        return category.parent.isNotNull();
    }

    private BooleanExpression eqCode(String code) {
        return category.code.eq(code);
    }

    private BooleanExpression isUsed() {
        return category.isUsed.isTrue();
    }
}

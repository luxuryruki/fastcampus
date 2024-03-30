package com.fastcampus.board.repository;

import com.fastcampus.board.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource // rest data 전략상 노출 시킬 리포지토리
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}

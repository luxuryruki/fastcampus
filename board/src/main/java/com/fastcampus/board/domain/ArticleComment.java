package com.fastcampus.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes ={
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
}) // 검색이 가능하게 할 필드 설정
@EntityListeners(AuditingEntityListener.class) // auditing 사용을 위해 필수
@Entity
public class ArticleComment {

    @Id //primary 키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 - mySql에서 id를 Auto로 두면 안됨 반드시  GenerationType.IDENTITY
    private Long id;
    @Setter
    @ManyToOne(optional = false) // optional은 해당 객체가 필수 여부를 묻는다. 댓글은 반드시 게시물이있어야 하기때문에 필수 요소. 추가적으로 cascade 옵셥을 줄수 있으나, 댓글이 삭제된다고해서 게시물이 삭제되는것이 아니기때문에 넣지 않아도 된다. defaul는 cascade = false
    private Article article; // 게시글 (ID)
    @Setter
    @Column(nullable = false, length = 500)
    private String content; // 본문


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // 날짜 포맷 도와준다
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일
    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy; //생성자 -> 사용자 정보를 넣어주기 위해 JpaConfig에서 auditorAware로 찾는다.
    @LastModifiedDate
    private LocalDateTime modifiedAt; //수정일
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // 날짜 포맷 도와준다
    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy; // 수정자 -> 사용자 정보를 넣어주기 위해 JpaConfig에서 auditorAware로 찾는다.


    protected ArticleComment() {}

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null & id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package com.fastcampus.board.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes ={
        @Index(columnList = "title"),
        @Index(columnList = "hashTag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
}) // 검색이 가능하게 할 필드 설정
@EntityListeners(AuditingEntityListener.class) // auditing 사용을 위해 필수
@Entity
public class Article {

    @Id //primary 키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 - mySql에서 id를 Auto로 두면 안됨 반드시  GenerationType.IDENTITY
    private Long id;
    @Setter // 도메인에 설정하지 않는 이유는 수정이 안되면 필드들이 있기 때문에 - ex) Id, 생성일, 생성자 등
    @Column(nullable = false)
    private String title; // 재목
    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 본문
    @Setter //@Column 생략가능 nullable = True
    private String hashtag; // 태그

    @ToString.Exclude // 순환 참조를 방지하기 위해서.
    @OrderBy("id") // 정렬기준은 id
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // article 테이블로부터 오는것 cascade 는 게시물이 삭제 될때 댓글도 전부 삭제하기 위함. 하지만 실무에서는 일부러 cascading 설정을 안하기도 함 (웒치않은 데이터 소실 및 마이그레이션의 어려움이 있을 수 있기 때문에)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

/*
Auditing 을 활용하여 자동으로 세팅할 수 있어야한다.
@CreatedDate
@CreatedBy
@LastModifiedDate
@LastModifiedBy
*/
    @CreatedDate
    private LocalDateTime createdAt; // 생성일
    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy; //생성자 -> 사용자 정보를 넣어주기 위해 JpaConfig에서 auditorAware로 찾는다.
    @LastModifiedDate
    private LocalDateTime modifiedAt; //수정일
    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy; // 수정자 -> 사용자 정보를 넣어주기 위해 JpaConfig에서 auditorAware로 찾는다.


    protected Article (){} // 기본 생성자 최소한 protect까지는 열어줘야 함. private은 허용안함

    private Article(String title, String content, String hashtag) { // public 안하는 이유는 팩토리 메서드를 활용해서 생성할 수 있도록 하기 위함
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // factory 메서드 - 다른  곳에서 new를 통해 객체를 생성하지 않아도 쉽게게 생성할 수 있게 도와줌
    public static Article of(String title, String content, String hashTag) {
        return new Article(title, content, hashTag);
    }
    //@EqualsAndHashCode 각각의 데이터 들의 중복확인이 나 정렬 등을 위한 롬복의 어노테이션
    // @EqualsAndHashCode 를 클래스 단위러 설정하게 되면 프로퍼티 전부 비교하기 때문에 성능이 떨어지며, entity의 id는 유니크해서 이것만 비교하면 된다 그래서 도메인에 한해서는 직접 구현해준다.
    // 엔티티를 데이터베이스에 영속화 시키고, 연결짓고 사용하는 환경에서 서로 다른 두 엔티티(로우)가 같은 조건이 무엇인가에 대한 질문을 equals가 대답 하고해준다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null &&  id.equals(article.id); // id가 부여되지 않았다(영속화 되지 않았다)라고 하면 동등성 검사자체가 의미가 없는 것으로 보고 처리하지 않겠다 라고 처리
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Article article = (Article) o;
//        return id != null &&  id.equals(article.id); // id가 부여되지 않았다(영속화 되지 않았다)라고 하면 동등성 검사자체가 의미가 없는 것으로 보고 처리하지 않겠다 라고 처리
//    }




    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

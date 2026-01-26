package com.example.springboot.repository;

import com.example.springboot.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // JPA 메서드 이름 규칙에 따라 메서드를 작성하면 자동으로 구현됩니다.
    // JPAREPOSITORY에서 제공하는 기본 메서드 외에 사용자 정의 메서드를 추가할 수 있습니다.

    MemberEntity findByName(String name);



}

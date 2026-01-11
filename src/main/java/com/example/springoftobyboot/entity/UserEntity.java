package com.example.springoftobyboot.entity;

import com.example.springoftobyboot.code.Level;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA는 기본 생성자가 필요하며, protected로 설정하여 외부에서 직접 호출하지 못하게 함
@AllArgsConstructor //  모든 필드를 매개변수로 받는 생성자 생성
@Builder    // 빌더 패턴을 사용하여 객체 생성
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) /** MySQL의 AUTO_INCREMENT와 매핑 */
    private Long id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;

    public static UserEntity createUser(String name, String password) {
        return UserEntity.builder()
                .name(name)
                .password(password)
                .build();
    }

    public static UserEntity setLevel(Level value) {
        switch (value) {
            case BASIC: UserEntity.builder().level(value).build();
            case SILVER: UserEntity.builder().level(value).build();
            case GOLD: UserEntity.builder().level(value).build();
            default: throw new AssertionError("Unknown value: " + value);
        }
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if(nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        }
        else {
            this.level = nextLevel;
        }
    }
}

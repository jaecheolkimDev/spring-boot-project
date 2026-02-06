package com.example.service;

import com.example.entity.ShoppingMemberEntity;
import com.example.repository.ShoppingMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
/**
 * 비즈니스 로직을 담당하는 서비스 계층 클래스에 @Transactional 어노테이션을 선언합니다.
 * 로직을 처리하다가 에러가 발생하였다면, 변경된 데이터를 로직을 수행하기 이전 상태로 콜백 시켜줍니다.
 */
@Transactional
public class ShoppingMemberService implements UserDetailsService {
    @Autowired
    private ShoppingMemberRepository shoppingMemberRepository;

    public ShoppingMemberEntity saveMember(ShoppingMemberEntity member) {
        validateDuplicateMember(member);
        return shoppingMemberRepository.save(member);
    }

    private void validateDuplicateMember(ShoppingMemberEntity member) {
        ShoppingMemberEntity findMember = shoppingMemberRepository.findByEmail(member.getEmail());
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ShoppingMemberEntity shoppingMemberEntity = shoppingMemberRepository.findByEmail(email);

        if(shoppingMemberEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(shoppingMemberEntity.getEmail())
                .password(shoppingMemberEntity.getPassword())
                .roles(shoppingMemberEntity.getRole().toString())
                .build();
    }
}

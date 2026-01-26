package com.example.springboot.service;

import com.example.springboot.code.Level;
import com.example.springboot.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.example.springboot.repository.MemberRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JavaMailSender mailSender;

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    public MemberEntity saveUser(String name, String password) {
        MemberEntity member = MemberEntity.createUser(name, password);
        return memberRepository.save(member);
    }

    public void deleteAll() {
        memberRepository.deleteAll();
    }

    public long getCount() {
        return memberRepository.count();
    }

    public void upgradeLevels() {
        List<MemberEntity> members = memberRepository.findAll();
        for(MemberEntity member : members) {
            if(canUpgradeLevel(member)) {
                upgradeLevel(member);
            }
        }
    }

    private boolean canUpgradeLevel(MemberEntity member) {
        Level currentLevel = member.getLevel();
        switch (currentLevel) {
            case BASIC: return (member.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (member.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    private void upgradeLevel(MemberEntity member) {
        member.upgradeLevel();
        memberRepository.save(member);
    }

    private void sendUpgradeEMail(MemberEntity member) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jcdoom@naver.com");
        message.setTo("받는사람 이메일");
        message.setSubject("제목");
        message.setText("내용");
        mailSender.send(message);
    }
}

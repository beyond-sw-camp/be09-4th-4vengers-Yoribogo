package com.avengers.yoribogo.user.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.dto.UserDTO;
import com.avengers.yoribogo.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EmailVerificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;  // StringRedisTemplate 사용

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private final long VERIFICATION_CODE_TTL = 5; // 5분
    private final long COOLDOWN_SECONDS = 30; // 30초 쿨다운

    public void sendVerificationEmail(String email) {
        if (isCooldown(email)) {
            throw new CommonException(ErrorCode.TOO_MANY_REQUESTS); //필기. 30초 쿨다운 제한
        }

        String verificationCode = generateVerificationCode();

        //필기. HTML 형식의 이메일 전송을 위한 객체
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("요리보고 이메일 인증 코드");

            //필기. HTML 형식의 이메일 내용 작성
            String content = "<div style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<h2 style='color: #FF7D7D;'>안녕하세요,</h2>"
                    + "<p><strong>Yoribogo</strong>서비스팀 입니다!</p>"
                    + "<p>이메일 인증을 완료하시려면 아래의 <strong style='color: #FF7D7D;'>6자리 인증 코드</strong>를 입력해 주세요:</p>"
                    + "<div style='margin: 20px 0; padding: 10px; border: 2px solid #FF7D7D; display: inline-block; font-size: 1.5em;'>"
                    + verificationCode + "</div>"
                    + "<p>이 인증 코드는 발송된 시점으로부터 <strong>5분간 유효</strong>합니다.</p>"
                    + "<p>문의사항이 있으시면 언제든지 저희 팀으로 연락 주시기 바랍니다.</p>"
                    + "<br>"
                    + "<p>감사합니다.<br>Yoribogo 팀 드림</p>"
                    + "<hr style='border: none; border-top: 1px solid #ddd;'>"
                    + "<p style='font-size: 0.9em; color: #999;'>이 메일은 발신 전용입니다. 답장을 보내지 말아 주세요.</p>"
                    + "</div>";

            // HTML 내용을 설정
            helper.setText(content, true);

        } catch (MessagingException e) {
            e.printStackTrace();
            // 예외 처리 로직 추가
        }

        mailSender.send(message);
        saveVerificationCode(email, verificationCode);
        saveCooldownTimestamp(email); //필기. 쿨다운 타임스탬프 저장
    }

    //필기. 해당 이메일의 코드가 일치하는지 확인하는 코드
    public boolean verifyCode(String email, String code) {
        String savedCode = stringRedisTemplate.opsForValue().get(email);  //필기. Redis에서 코드 가져오기

        // 필기. 인증 코드가 일치하면 Redis에서 해당 키값(email)을 True로 설정
        if (savedCode != null && savedCode.equals(code)) {
            // 인증 성공 시 Redis에 True 저장하고 TTL을 1시간으로 설정
            stringRedisTemplate.opsForValue().set(email, "True", 1, TimeUnit.HOURS);
            return true;
        } else {
            return false;
        }
    }

    //필기. Redis에 코드 저장
    private void saveVerificationCode(String email, String code) {
        stringRedisTemplate.opsForValue().set(email, code, VERIFICATION_CODE_TTL, TimeUnit.MINUTES);
    }

    //필기. 6자리 랜덤문자열 발생 ()
    private String generateVerificationCode() {
        // 사용할 수 있는 문자들: 대문자, 소문자, 숫자, 특수문자
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }

    //필기. 이메일 전송 쿨다운 체크
    private boolean isCooldown(String email) {
        String lastSentTime = stringRedisTemplate.opsForValue().get(email + ":cooldown"); //필기. 쿨다운 키

        if (lastSentTime == null) {
            return false;
        }

        long lastSentTimestamp = Long.parseLong(lastSentTime);
        long currentTimestamp = System.currentTimeMillis() / 1000; // 필기. 초 단위 시간\

        log.error("재전송 가능 남은 시간: {}", currentTimestamp);

        return currentTimestamp - lastSentTimestamp < COOLDOWN_SECONDS;
    }

    //필기. 쿨다운 타임스탬프 저장
    private void saveCooldownTimestamp(String email) {
        long currentTimestamp = System.currentTimeMillis() / 1000; // 필기. 초 단위 시간
        stringRedisTemplate.opsForValue()
                .set(email + ":cooldown", String.valueOf(currentTimestamp), COOLDOWN_SECONDS, TimeUnit.SECONDS); // 필기. TTL 30초
    }

    //필기. 닉네임의 회원의 이메일의 코드가 일치하는지 확인하는 코드
    public UserEntity verifyUserNicknameCode(String nickname, String email, String code) {
        // Redis에서 저장된 코드 가져오기
        String savedCode = stringRedisTemplate.opsForValue().get(email);

        // 사용자 정보 조회
        UserEntity userEntity =userRepository.findByNickname(nickname)
                      .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 인증 코드가 없거나 일치하지 않으면 예외 발생
        if (savedCode == null || !savedCode.equals(code)) {
            throw new CommonException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        // 인증 성공 시 Redis에 "verified" 저장하고 TTL을 1시간으로 설정
        stringRedisTemplate.opsForValue().set(email, "verified", 1, TimeUnit.HOURS);

        return userEntity;
    }
}

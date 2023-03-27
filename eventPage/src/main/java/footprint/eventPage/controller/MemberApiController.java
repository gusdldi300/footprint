package footprint.eventPage.controller;

import footprint.eventPage.dto.AuthenticResponseDto;
import footprint.eventPage.email.EmailMessage;
import footprint.eventPage.email.EmailService;
import footprint.eventPage.entity.Member;
import footprint.eventPage.dto.MemberCodeRequestDto;
import footprint.eventPage.dto.MemberRegisterRequestDto;
import footprint.eventPage.dto.MemberResponseDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import footprint.eventPage.service.MemberService;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    private final EmailService emailService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/api/members")
    public AuthenticResponseDto registerMember(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("message = {}", messageBody);

        MemberRegisterRequestDto memberRequest = this.objectMapper.readValue(messageBody, MemberRegisterRequestDto.class);
        log.info("email = {}, agreed = {}", memberRequest.getEmail(), memberRequest.isAgreed());

        Member MemberOrNull = this.memberService.getMemberByEmailOrNull(memberRequest.getEmail());
        if (MemberOrNull != null) {
            return new AuthenticResponseDto(false);
        }

        String authenticCodeInString = this.memberService.createAuthenticCode();
        log.info("authenticCodeInString: {}", authenticCodeInString);

        final String mailSubject = "[fooprint story] 요청하신 본인확인 인증번호입니다.";
        EmailMessage emailMessage = new EmailMessage(memberRequest.getEmail(), mailSubject, authenticCodeInString);
        this.emailService.sendTemplateMail(emailMessage);

        HttpSession session = request.getSession();
        session.setAttribute(authenticCodeInString, memberRequest);
        session.setMaxInactiveInterval(180); //새션 타임아웃 시간 설정: 3분

            log.info("session member email = {}", ((MemberRegisterRequestDto) session.getAttribute(authenticCodeInString)).getEmail());

        return new AuthenticResponseDto(true);
}

    @PostMapping("/api/members/code")
    public MemberResponseDto validateMemberCode(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        MemberCodeRequestDto memberCodeRequestDto  = this.objectMapper.readValue(messageBody, MemberCodeRequestDto.class);
        log.info("authenticCode = {}", memberCodeRequestDto.getAuthenticCode());

        String authenticCode = memberCodeRequestDto.getAuthenticCode();
        HttpSession sessionOrNull = request.getSession(false);
        if (sessionOrNull == null) {
            return new MemberResponseDto(false, (long) -1);
        }

        MemberRegisterRequestDto memberRequestOrNull = (MemberRegisterRequestDto) sessionOrNull.getAttribute(authenticCode);
        if (memberRequestOrNull == null) {
            log.info("Session Timeout or Wrong number");

            return new MemberResponseDto(false, (long) -1);
        }
        sessionOrNull.invalidate(); //확인필요

        this.memberService.register(new Member(memberRequestOrNull.getEmail(), memberRequestOrNull.isAgreed()));
        Member memberOrNull = this.memberService.getMemberByEmailOrNull(memberRequestOrNull.getEmail());

        return new MemberResponseDto(true, memberOrNull.getId());
    }

}

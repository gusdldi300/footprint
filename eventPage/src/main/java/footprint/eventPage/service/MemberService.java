package footprint.eventPage.service;

import footprint.eventPage.entity.Member;
import footprint.eventPage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public boolean register(final Member member) {
        if (this.getMemberByEmailOrNull(member.getEmail()) != null) {
            return false;
        }

        memberRepository.save(member);
        return true;
    }

    public Member getMemberByEmailOrNull(String memberEmail) {
        //실무에서는, 멀티스레드 등을 고려해서, 멤버의 이름을 유니크 제약조건으로 잡는 것을 권장한다
        List<Member> foundMember = this.memberRepository.findByEmail(memberEmail);
        if (foundMember.isEmpty()) {
            return null;
        }

        return foundMember.get(0);
    }

    public String createAuthenticCode() {
        final int CODE_LENGTH = 6;

        String authentciCodeInString = UUID.randomUUID().toString().substring(0, CODE_LENGTH);

        return authentciCodeInString;
    }
}

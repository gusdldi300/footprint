package footprint.eventPage.repository;

import footprint.eventPage.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    
    @Test
    public void testMember() {
        Member member = new Member("memberA", false);
        Member savedMember = this.memberRepository.save(member);
        Member findMember =  this.memberRepository.findById(savedMember.getId());
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        Assertions.assertThat(findMember.isAgreed()).isEqualTo(member.isAgreed());
        Assertions.assertThat(findMember).isEqualTo(member); // 엔티티 동일성 보장
    }
}
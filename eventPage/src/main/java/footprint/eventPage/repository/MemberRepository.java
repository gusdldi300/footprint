package footprint.eventPage.repository;

import footprint.eventPage.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(final Member member) {
        // 영속화: 영속성 컨텍스트 내에 키(PK), 값(객체)이 생성됨
        // 트랜잭션 커밋 되는 시점에, DB에 저장
        em.persist(member);

        return member;
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }
}

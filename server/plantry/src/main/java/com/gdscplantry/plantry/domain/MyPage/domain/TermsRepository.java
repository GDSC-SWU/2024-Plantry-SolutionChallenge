package com.gdscplantry.plantry.domain.MyPage.domain;

import com.gdscplantry.plantry.domain.MyPage.vo.TermsItemVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Long> {
    // Find terms with title
    @Query(value = "select new com.gdscplantry.plantry.domain.MyPage.vo.TermsItemVo(t.createdAt, t.updatedAt, t.title, t.content) " +
            "from Terms t where t.title = :title " +
            "order by t.createdAt desc, t.updatedAt desc")
    TermsItemVo findByTitleOrderByCreatedAtDescUpdatedAtDescWithJPQL(String title);
}

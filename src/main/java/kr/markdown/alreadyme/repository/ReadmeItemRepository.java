package kr.markdown.alreadyme.repository;

import kr.markdown.alreadyme.domain.model.ReadmeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadmeItemRepository extends JpaRepository<ReadmeItem, Long> {
}

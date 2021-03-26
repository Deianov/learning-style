package bg.geist.repository;

import bg.geist.domain.entity.DictionaryCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryCollectionRepository extends JpaRepository<DictionaryCollection, Long> {
}

package bg.geist.repository;

import bg.geist.domain.entity.Prop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropRepository extends JpaRepository<Prop, Long> {
}

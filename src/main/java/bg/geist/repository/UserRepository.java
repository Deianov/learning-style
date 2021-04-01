package bg.geist.repository;

import bg.geist.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity, Long>  {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<UserEntity> findByUsername(String userName);
    Optional<UserEntity> findByEmail(String email);
}
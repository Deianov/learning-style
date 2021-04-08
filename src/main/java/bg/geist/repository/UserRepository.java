package bg.geist.repository;

import bg.geist.domain.entity.UserEntity;
import bg.geist.domain.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity, Long>  {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByIdAndRolesContains(Long id, UserRoleEntity role);

    Optional<UserEntity> findByUsername(String userName);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByProfileId(Long id);
}
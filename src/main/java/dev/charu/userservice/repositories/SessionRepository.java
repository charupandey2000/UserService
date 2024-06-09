package dev.charu.userservice.repositories;

import dev.charu.userservice.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByTokenAndUser_Id(String token, Long userId);
}

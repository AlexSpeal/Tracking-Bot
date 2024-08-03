package edu.java.domain.implementations.jpa;

import edu.java.entities.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface JpaChatRepository extends JpaRepository<ChatEntity, Long> {

}

package com.siuuuuu.commodeami.user.command.domain.repository;

import com.siuuuuu.commodeami.user.command.aggregate.dto.UserDTO;
import com.siuuuuu.commodeami.user.command.aggregate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(Long userId);

    User findUserByEmail(String email);

    User findByEmail(String email);
}

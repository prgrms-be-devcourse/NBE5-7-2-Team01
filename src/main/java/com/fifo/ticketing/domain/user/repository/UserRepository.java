package com.fifo.ticketing.domain.user.repository;

import com.fifo.ticketing.domain.user.entity.Role;
import com.fifo.ticketing.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Page<User> findByUsernameContaining(String userName, Pageable pageable);

    Page<User> findByRole(Role role, Pageable pageable);

    Page<User> findByUsernameContainingAndRole(String username, Role role, Pageable pageable);

}

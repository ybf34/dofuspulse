package com.dofuspulse.api.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserPrincipal, Long> {

  Optional<UserPrincipal> findByEmail(String email);
}

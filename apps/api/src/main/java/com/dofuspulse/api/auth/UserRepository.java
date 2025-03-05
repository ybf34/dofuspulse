package com.dofuspulse.api.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserPrincipal, Long> {

  @Query("SELECT u FROM UserPrincipal u LEFT JOIN FETCH u.socialLogins WHERE u.email = ?1")
  Optional<UserPrincipal> findByEmail(String email);

  boolean existsByEmail(String email);

  Optional<UserPrincipal> findBySocialLoginsProviderAndSocialLoginsProviderId(
      String provider,
      String providerId);
}

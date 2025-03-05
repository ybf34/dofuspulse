package com.dofuspulse.api.auth.oauth2;


import com.dofuspulse.api.auth.UserPrincipal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_social_logins", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"provider", "provider_id"})
})
public class UserSocialLogin implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserPrincipal user;

  @Column(nullable = false)
  private String provider;

  @Column(name = "provider_id", nullable = false)
  private String providerId;

  @CreationTimestamp
  private Instant createdAt;

  @UpdateTimestamp
  private Instant updatedAt;

}
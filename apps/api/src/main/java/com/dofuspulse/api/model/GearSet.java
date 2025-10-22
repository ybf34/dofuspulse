package com.dofuspulse.api.model;

import com.dofuspulse.api.auth.UserPrincipal;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gear_set")
public class GearSet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private List<String> tags;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserPrincipal userPrincipal;

  @ManyToOne
  @JoinColumn(name = "character_class_id", nullable = false, columnDefinition = "char(1)")
  private CharacterClass characterClass;

  @Column(name = "character_gender")
  private String characterGender;

  @OneToMany(mappedBy = "gearSet", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GearSetSlot> slots = new ArrayList<>();

  @CreationTimestamp
  private Instant createdAt;

  @UpdateTimestamp
  private Instant updatedAt;
}

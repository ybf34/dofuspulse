package com.dofuspulse.api.gearset.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.gearset.dto.CharacterClassName;
import com.dofuspulse.api.gearset.dto.CreateGearSetRequest;
import com.dofuspulse.api.gearset.dto.GearSetDto;
import com.dofuspulse.api.gearset.dto.UpdateGearSetRequest;
import com.dofuspulse.api.gearset.service.GearSetServiceImpl;
import com.dofuspulse.api.model.CharacterClass;
import com.dofuspulse.api.model.GearSet;
import com.dofuspulse.api.repository.CharacterClassRepository;
import com.dofuspulse.api.repository.GearSetRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GearSetServiceUnitTest {

  @Mock
  GearSetRepository gearSetRepository;

  @Mock
  CharacterClassRepository characterClassRepository;

  @InjectMocks
  GearSetServiceImpl gearSetService;

  UserPrincipal user;

  @BeforeEach
  void setUp() {
    user = new UserPrincipal();
    user.setId(1L);
    user.setEmail("user@mail.com");
    user.setRole(Role.USER);
  }

  @Test
  void shouldFindGearSetById() {
    GearSet gearSet = new GearSet();
    gearSet.setId(1L);
    CharacterClass characterClass = new CharacterClass();
    characterClass.setName(CharacterClassName.CRA);

    gearSet.setCharacterClass(characterClass);
    gearSet.setUserPrincipal(user);

    when(gearSetRepository.findById(1L)).thenReturn(Optional.of(gearSet));

    GearSetDto result = gearSetService.findById(1L, user);

    assertThat(result).isNotNull().satisfies(dto -> {
      assertThat(dto.id()).isEqualTo(1L);
      assertThat(dto.characterClass().name()).isEqualTo(CharacterClassName.CRA);
    });

    verify(gearSetRepository).findById(1L);
  }

  @Test
  void shouldReturnEmptyWhenGearSetNotFoundById() {
    when(gearSetRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> gearSetService.findById(1L, user)).isInstanceOf(NoSuchElementException.class);

    verify(gearSetRepository, times(1)).findById(1L);
  }

  @Test
  void shouldFindUserGearSets() {
    GearSet gearSet = new GearSet();
    gearSet.setId(1L);
    CharacterClass characterClass = new CharacterClass();
    characterClass.setName(CharacterClassName.CRA);
    gearSet.setCharacterClass(characterClass);
    gearSet.setUserPrincipal(user);

    when(gearSetRepository.findByUserPrincipalIdOrderByUpdatedAtDesc(user.getId())).thenReturn(List.of(gearSet));

    List<GearSetDto> result = gearSetService.findUserGearSets(user);

    assertThat(result).hasSize(1).first().satisfies(dto -> {
      assertThat(dto.id()).isEqualTo(1L);
      assertThat(dto.characterClass().name()).isEqualTo(CharacterClassName.CRA);
    });

    verify(gearSetRepository, times(1)).findByUserPrincipalIdOrderByUpdatedAtDesc(user.getId());
  }

  @Test
  void shouldCreateGearSet() {
    CreateGearSetRequest request = new CreateGearSetRequest("New Set", "cra", "f", List.of("tag1"));

    CharacterClass characterClass = new CharacterClass();
    characterClass.setName(CharacterClassName.CRA);

    GearSet saved = new GearSet();
    saved.setId(42L);
    saved.setTitle("New Set");
    saved.setCharacterClass(characterClass);
    saved.setCharacterGender("f");
    saved.setTags(List.of("tag1"));
    saved.setUserPrincipal(user);

    when(characterClassRepository.findByName(CharacterClassName.CRA)).thenReturn(Optional.of(characterClass));
    when(gearSetRepository.save(any(GearSet.class))).thenReturn(saved);

    GearSetDto result = gearSetService.createGearSet(request, user);

    assertThat(result).satisfies(dto -> {
      assertThat(dto.id()).isEqualTo(42L);
      assertThat(dto.title()).isEqualTo("New Set");
      assertThat(dto.characterClass().name()).isEqualTo(CharacterClassName.CRA);
      assertThat(dto.characterGender()).isEqualTo("f");
      assertThat(dto.tags()).containsExactly("tag1");
    });

    verify(characterClassRepository).findByName(CharacterClassName.CRA);
    verify(gearSetRepository).save(any(GearSet.class));
  }

  @Test
  void shouldUpdateGearset() {

    String newTitle = "gearset updated";
    CharacterClassName newClass = CharacterClassName.valueOf("CRA");
    String newGenre = "m";
    List<String> newTags = List.of("tag1", "tag2");

    UpdateGearSetRequest updateGearSetRequest = new UpdateGearSetRequest(newTitle, newClass.toString(), newGenre,
        newTags);

    CharacterClass characterClass = new CharacterClass();
    characterClass.setName(CharacterClassName.CRA);

    GearSet saved = new GearSet();
    saved.setId(42L);
    saved.setTitle(newTitle);
    saved.setCharacterClass(characterClass);
    saved.setCharacterGender(newGenre);
    saved.setTags(newTags);
    saved.setUserPrincipal(user);

    when(gearSetRepository.findById(42L)).thenReturn(Optional.of(saved));
    when(characterClassRepository.findByName(newClass)).thenReturn(Optional.of(characterClass));

    GearSetDto result = gearSetService.updateGearSet(42L, updateGearSetRequest, user);

    assertThat(result).satisfies(dto -> {
      assertThat(dto.id()).isEqualTo(42L);
      assertThat(dto.title()).isEqualTo(newTitle);
      assertThat(dto.characterClass().name()).isEqualTo(newClass);
      assertThat(dto.characterGender()).isEqualTo(newGenre);
      assertThat(dto.tags()).usingRecursiveAssertion().isEqualTo(newTags);
    });

    verify(characterClassRepository).findByName(CharacterClassName.CRA);
  }

  @Test
  void shouldDeleteOwnedGearSet() {
    GearSet gearSet = new GearSet();
    gearSet.setId(1L);
    gearSet.setUserPrincipal(user);

    when(gearSetRepository.findById(1L)).thenReturn(Optional.of(gearSet));

    gearSetService.deleteGearSet(1L, user);

    verify(gearSetRepository).deleteById(1L);
  }

  @Test
  void shouldThrowWhenDeletingNonExistentGearSet() {
    when(gearSetRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> gearSetService.deleteGearSet(1L, user)).isInstanceOf(NoSuchElementException.class);

    verify(gearSetRepository, never()).deleteById(any());
  }

  @Test
  void shouldThrowWhenDeletingGearSetOwnedByAnotherUser() {
    UserPrincipal otherUser = new UserPrincipal();
    otherUser.setId(2L);

    GearSet gearSet = new GearSet();
    gearSet.setId(1L);
    gearSet.setUserPrincipal(otherUser);

    when(gearSetRepository.findById(1L)).thenReturn(Optional.of(gearSet));

    assertThatThrownBy(() -> gearSetService.deleteGearSet(1L, user)).isInstanceOf(NoSuchElementException.class);

    verify(gearSetRepository, never()).deleteById(any());
  }

}

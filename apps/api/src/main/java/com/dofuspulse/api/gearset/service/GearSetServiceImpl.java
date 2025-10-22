package com.dofuspulse.api.gearset.service;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.gearset.dto.CharacterClassName;
import com.dofuspulse.api.gearset.dto.CreateGearSetRequest;
import com.dofuspulse.api.gearset.dto.GearSetDto;
import com.dofuspulse.api.gearset.dto.UpdateGearSetRequest;
import com.dofuspulse.api.gearset.service.contract.GearSetService;
import com.dofuspulse.api.model.CharacterClass;
import com.dofuspulse.api.model.GearSet;
import com.dofuspulse.api.repository.CharacterClassRepository;
import com.dofuspulse.api.repository.GearSetRepository;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GearSetServiceImpl implements GearSetService {

  private final GearSetRepository gearSetRepository;
  private final CharacterClassRepository characterClassRepository;

  @Override
  @Transactional(readOnly = true)
  public GearSetDto findById(Long id, UserPrincipal user) {
    return gearSetRepository.findById(id)
        .filter(g -> user.getId().equals(g.getUserPrincipal().getId()))
        .map(GearSetDto::new)
        .orElseThrow(() -> new NoSuchElementException("Gearset not found."));
  }

  @Override
  @Transactional(readOnly = true)
  public List<GearSetDto> findUserGearSets(UserPrincipal user) {
    return gearSetRepository.findByUserPrincipalIdOrderByUpdatedAtDesc(user.getId()).stream().map(GearSetDto::new)
        .toList();
  }

  @Override
  @Transactional
  public GearSetDto createGearSet(CreateGearSetRequest gearSetRequest, UserPrincipal user) {
    GearSet newGearSet = new GearSet();
    newGearSet.setUserPrincipal(user);
    newGearSet.setTitle(gearSetRequest.title());
    newGearSet.setTags(gearSetRequest.tags());

    CharacterClass characterClass =
        characterClassRepository.findByName(
                CharacterClassName.valueOf(gearSetRequest.characterClass().toUpperCase()))
            .orElseThrow(() -> new NoSuchElementException(
                "Character class with name " + gearSetRequest.characterClass()
                    + " doesn't exist"));

    newGearSet.setCharacterClass(characterClass);
    newGearSet.setCharacterGender(gearSetRequest.characterGender());
    return new GearSetDto(gearSetRepository.save(newGearSet));
  }

  @Override
  @Transactional
  public GearSetDto updateGearSet(Long id, UpdateGearSetRequest request, UserPrincipal user) {

    GearSet gearset = gearSetRepository.findById(id)
        .filter(g -> g.getUserPrincipal().getId().equals(user.getId()))
        .orElseThrow(() -> new NoSuchElementException("GearSet not found."));

    if (request.characterClass() != null) {
      CharacterClass characterClass = characterClassRepository.findByName(
              CharacterClassName.valueOf(request.characterClass().toUpperCase()))
          .orElseThrow(() -> new NoSuchElementException(
              "Character class " + request.characterClass() + " doesn't exist"));
      gearset.setCharacterClass(characterClass);
    }

    if (request.title() != null) {
      gearset.setTitle(request.title());
    }

    if (request.characterGender() != null) {
      gearset.setCharacterGender(request.characterGender());
    }

    if (request.tags() != null) {
      gearset.setTags(request.tags());
    }

    gearset.setUpdatedAt(Instant.now());

    return new GearSetDto(gearset);
  }

  @Override
  @Transactional
  public void deleteGearSet(Long id, UserPrincipal user) {
    gearSetRepository.findById(id)
        .filter(g -> g.getUserPrincipal().getId().equals(user.getId()))
        .orElseThrow(() -> new NoSuchElementException("GearSet not found."));

    gearSetRepository.deleteById(id);
  }

}

package com.dofuspulse.api.gearset.service;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.gearset.dto.CreateGearSetRequest;
import com.dofuspulse.api.gearset.dto.GearSetDto;
import com.dofuspulse.api.gearset.service.contract.GearSetService;
import com.dofuspulse.api.model.CharacterClass;
import com.dofuspulse.api.model.GearSet;
import com.dofuspulse.api.repository.CharacterClassRepository;
import com.dofuspulse.api.repository.GearSetRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
  public Optional<GearSetDto> findById(Long id) {
    return gearSetRepository.findById(id).map(GearSetDto::new);
  }

  @Override
  @Transactional(readOnly = true)
  public List<GearSetDto> findUserGearSets(UserPrincipal user) {
    return gearSetRepository.findByUserPrincipalId(user.getId()).stream().map(GearSetDto::new)
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
        characterClassRepository.findByName(gearSetRequest.characterClass())
            .orElseThrow(() -> new NoSuchElementException(
                "Character class with name " + gearSetRequest.characterClass()
                    + " doesn't exist"));

    newGearSet.setCharacterClass(characterClass);
    newGearSet.setCharacterGender(gearSetRequest.characterGender());
    return new GearSetDto(gearSetRepository.save(newGearSet));
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

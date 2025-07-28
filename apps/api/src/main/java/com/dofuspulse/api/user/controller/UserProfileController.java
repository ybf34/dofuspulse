package com.dofuspulse.api.user.controller;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.user.dto.UserProfileDto;
import com.dofuspulse.api.user.service.UserProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserProfileController {

  private final UserProfileServiceImpl userProfileService;

  @GetMapping("/user/me")
  public ResponseEntity<UserProfileDto> getUserProfile(@AuthenticationPrincipal UserPrincipal principal) {
    return ResponseEntity.ok(userProfileService.getUserProfile(principal));
  }
}

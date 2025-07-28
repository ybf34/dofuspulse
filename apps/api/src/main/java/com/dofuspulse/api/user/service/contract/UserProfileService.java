package com.dofuspulse.api.user.service.contract;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.user.dto.UserProfileDto;

public interface UserProfileService {

  UserProfileDto getUserProfile(UserPrincipal userPrincipal);
}

import type {
  DiscordUserAttributes,
  GoogleUserAttributes,
  UserDisplayProfile,
  UserProfile,
} from "@/types/types.ts";

import {discordAvatarBaseUrl} from "@/utils/constants.ts";

const getDiscordAvatarUrl = (
    userSocialId: string,
    avatarHash: string,
): string => {
  const format = avatarHash.startsWith("a_") ? "gif" : "png";
  return `${discordAvatarBaseUrl}/${userSocialId}/${avatarHash}.${format}`;
};

export const getUserProfilePicture = (
    userProfile: UserProfile | null,
): string | undefined => {
  if (!userProfile || !userProfile.attributes) {
    return undefined;
  }

  const attributes = userProfile.attributes;

  if ("sub" in attributes) {
    const googleAttrs = attributes as GoogleUserAttributes;
    return googleAttrs.picture || undefined;
  } else if ("id" in attributes && "username" in attributes) {
    const discordAttrs = attributes as DiscordUserAttributes;

    if (discordAttrs.avatar && discordAttrs.id) {
      return getDiscordAvatarUrl(discordAttrs.id, discordAttrs.avatar);
    }
    return undefined;
  }

  return undefined;
};

export const normalizeUserProfile = (
    userProfile: UserProfile | null,
): UserDisplayProfile | null => {
  if (!userProfile) {
    return null;
  }

  const email = userProfile.email ?? "";

  const avatar = getUserProfilePicture(userProfile);
  let name = "";

  if (userProfile.attributes) {
    const attributes = userProfile.attributes;
    if ("name" in attributes) {
      name = (attributes as GoogleUserAttributes).name;
    } else if ("id" in attributes && "username" in attributes) {
      const discordAttrs = attributes as DiscordUserAttributes;
      name = discordAttrs.global_name || discordAttrs.username;
    }
  }

  return {
    email,
    name,
    avatar,
  };
};

import type {
  GearSetDto,
  GearSetSlotDto,
  ItemDetailsDto,
  UserProfileDto,
} from "@/api/model";

export type GridValue = 1 | 2 | 3 | 4 | 5 | 6;

export type GridPosition = {
  colStart: GridValue;
  rowStart: GridValue;
};

export const GearsetSlotIdentifierValues = {
  HAT: "HAT",
  CLOAK: "CLOAK",
  AMULET: "AMULET",
  WEAPON: "WEAPON",
  LEGENDARY_WEAPON: "LEGENDARY_WEAPON",
  SHIELD: "SHIELD",
  RING_1: "RING_1",
  RING_2: "RING_2",
  BELT: "BELT",
  BOOTS: "BOOTS",
  PET_MOUNT: "PET_MOUNT",
  DOFUS_TROPHY_1: "DOFUS_TROPHY_1",
  DOFUS_TROPHY_2: "DOFUS_TROPHY_2",
  DOFUS_TROPHY_3: "DOFUS_TROPHY_3",
  DOFUS_TROPHY_4: "DOFUS_TROPHY_4",
  DOFUS_TROPHY_5: "DOFUS_TROPHY_5",
  DOFUS_TROPHY_6: "DOFUS_TROPHY_6",
} as const;

export type GearsetSlotIdentifier =
    (typeof GearsetSlotIdentifierValues)[keyof typeof GearsetSlotIdentifierValues];

export type GearSetSlotCellProps = {
  slotIdentifier: GearsetSlotIdentifier;
  itemDetails?: ItemDetailsDto | null;
};

export type CharacterClassType = {
  name: string;
  gender: string;
};
export type GearSetGridProps = {
  id: string;
  characterClass: CharacterClassType;
  gearsetSlots: GearSetSlotDto[];
};

export type GearSetCardProps = {
  onDelete: (id: number) => void;
  gearSet: GearSetDto;
};

export type OAuth2Provider = "discord" | "google";

export type DiscordUserAttributes = {
  id: string;
  username: string;
  avatar: string;
  discriminator: string;
  public_flags: number;
  flags: number;
  banner: string | null;
  accent_color: string | null;
  global_name: string | null;
  avatar_decoration_data: unknown | null;
  collectibles: unknown | null;
  banner_color: string | null;
  clan: unknown | null;
  primary_guild: unknown | null;
  mfa_enabled: boolean;
  locale: string;
  premium_type: number;
  email: string;
  verified: boolean;
};

export type GoogleUserAttributes = {
  sub: string;
  name: string;
  given_name: string;
  family_name: string;
  picture: string;
  email: string;
  email_verified: boolean;
};

export type UserProfile = Omit<UserProfileDto, "attributes"> & {
  attributes?: GoogleUserAttributes | DiscordUserAttributes | null;
};

export type UserDisplayProfile = {
  email: string;
  name?: string;
  avatar?: string;
};

export type CharacterClassNameType =
    | "cra"
    | "ecaflip"
    | "eniripsia"
    | "enutrof"
    | "feca"
    | "iop"
    | "osamodas"
    | "pandawa"
    | "roublard"
    | "sacrieur"
    | "sadida"
    | "sram"
    | "steamer"
    | "xelor"
    | "zobal";

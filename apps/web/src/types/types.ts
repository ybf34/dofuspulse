import type {APIItemDetails, APIUser, CharacterClassName,} from "@/services/api/api.types";

export type GridValue = 1 | 2 | 3 | 4 | 5 | 6;

export type GridPosition = {
	colStart: GridValue;
	rowStart: GridValue;
};

export type SlotConfig = {
  gridPosition: GridPosition;
  placeholderIcon: string;
};

export type CharacterClassType = {
  characterClass: CharacterClassName;
  gender: "m" | "f";
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

export type UserProfile = Omit<APIUser, "attributes"> & {
	attributes?: GoogleUserAttributes | DiscordUserAttributes | null;
};

export type UserDisplayProfile = {
	email: string;
	name?: string;
	avatar?: string;
};

export type ItemWithQuantity = Pick<
    APIItemDetails,
    "id" | "name" | "iconId"
> & {
  quantity?: number;
};

export const timeRanges = ["7d", "30d", "1y", "max"] as const;

export type TimeRange = (typeof timeRanges)[number];

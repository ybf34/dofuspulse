import { env } from "@/lib/env";
import type { OAuth2Provider } from "@/types/types";

export const getOAuth2AuthorizationUrl = (provider: OAuth2Provider): string => {
	return `${env.VITE_API_BASE_URL}/oauth2/authorization/${provider}`;
};

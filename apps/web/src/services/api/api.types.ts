import { z } from "zod";

export const ValidationErrorSchema = z.object({
	name: z.string(),
	reason: z.string(),
});

export const ProblemDetailSchema = z.object({
	title: z.string(),
	status: z.number(),
	detail: z.string(),
	type: z.string().optional(),
	instance: z.string().optional(),
	"validation-errors": z.array(ValidationErrorSchema).optional(),
});

export type ProblemDetail = z.infer<typeof ProblemDetailSchema>;

export const UserOAuth2AttributesSchema = z.record(z.unknown());
export const UserRoleSchema = z.enum(["ADMIN", "USER"]);
export const UserSocialLoginSchema = z.object({
	provider: z.string(),
	providerId: z.string(),
});

export const UserSchema = z.object({
	id: z.number(),
	email: z.string().email(),
	role: UserRoleSchema,
	socials: z.array(UserSocialLoginSchema).optional(),
	attributes: UserOAuth2AttributesSchema.optional(),
	createdAt: z.string().datetime(),
	updatedAt: z.string().datetime(),
});

export type APIUser = z.infer<typeof UserSchema>;

export const characterClassNames = [
	"CRA",
	"ECAFLIP",
	"ENIRIPSIA",
	"ENUTROF",
	"FECA",
	"IOP",
	"OSAMODAS",
	"PANDAWA",
	"ROUBLARD",
	"SACRIEUR",
	"SADIDA",
	"SRAM",
	"STEAMER",
	"XELOR",
	"ZOBAL",
] as const;

export const CharacterClassNameSchema = z.enum(characterClassNames);
export type CharacterClassName = (typeof characterClassNames)[number];

export const gearsetSlotTypeIdentifiers = [
	"HAT",
	"CLOAK",
	"AMULET",
	"WEAPON",
	"LEGENDARY_WEAPON",
	"SHIELD",
	"RING_1",
	"RING_2",
	"BELT",
	"BOOTS",
	"PET_MOUNT",
	"DOFUS_TROPHY_1",
	"DOFUS_TROPHY_2",
	"DOFUS_TROPHY_3",
	"DOFUS_TROPHY_4",
	"DOFUS_TROPHY_5",
	"DOFUS_TROPHY_6",
] as const;

export const GearsetSlotTypeIdentifierSchema = z.enum(
	gearsetSlotTypeIdentifiers,
);

export type GearsetSlotTypeIdentifier =
	(typeof gearsetSlotTypeIdentifiers)[number];

export const ItemEffectSchema = z.object({
	effectId: z.number(),
	minValue: z.number(),
	maxValue: z.number(),
});

export type ItemEffect = z.infer<typeof ItemEffectSchema>;
export const createPaginatedSchema = <T extends z.ZodTypeAny>(
	contentSchema: T,
) => {
	return z.object({
		content: z.array(contentSchema),
		page: z.object({
			size: z.number(),
			number: z.number(),
			totalElements: z.number(),
			totalPages: z.number(),
		}),
	});
};

export const ItemTypeSchema = z.object({
	id: z.number(),
	name: z.string(),
});
export type ItemType = z.infer<typeof ItemTypeSchema>;

export const ItemDetailsSchema = z.object({
	id: z.number(),
	name: z.string(),
	level: z.number(),
	itemTypeId: z.number(),
	iconId: z.number(),
	ingredientIds: z.array(z.number()),
	possibleEffects: z.array(ItemEffectSchema),
});

export type APIItemDetails = z.infer<typeof ItemDetailsSchema>;

export const GearSetSlotTypeSchema = z.object({
	id: z.number(),
	name: GearsetSlotTypeIdentifierSchema,
	itemTypes: z.array(ItemTypeSchema),
});

export type APIGearSetSlotType = z.infer<typeof GearSetSlotTypeSchema>;

export const GearSetSlotSchema = z.object({
	id: z.number(),
	itemDetails: ItemDetailsSchema,
	slotType: GearSetSlotTypeSchema,
});

export type APIGearsetSlot = z.infer<typeof GearSetSlotSchema>;

export const GearSetSchema = z.object({
	id: z.number(),
	title: z.string(),
	characterClass: z.object({
		name: CharacterClassNameSchema,
	}),
	characterGender: z.enum(["m", "f"]),
	tags: z.array(z.string()),
	slots: z.array(GearSetSlotSchema),
	createdAt: z.string().datetime(),
	updatedAt: z.string().datetime(),
});

export type APIGearset = z.infer<typeof GearSetSchema>;

export const EffectSchema = z.object({
	id: z.number(),
	descriptionTemplate: z.string(),
});

export type Effect = z.infer<typeof EffectSchema>;
export const ItemPriceSchema = z.object({
	prices: z.array(z.number()),
	itemId: z.number(),
	snapshotDate: z.string(),
});

export const ProfitMetricsEntrySchema = z.object({
	snapshotDate: z.string(),
	craftCost: z.number(),
	sellPrice: z.number(),
	profitMargin: z.number(),
	roi: z.number(),
});

export type ProfitMetricsEntry = z.infer<typeof ProfitMetricsEntrySchema>;

export type ItemProfitMetrics = ProfitMetricsEntry[];

export const ItemsProfitMetricsSchema = z.array(
	z.object({
		itemId: z.number(),
		profitMetrics: z.array(ProfitMetricsEntrySchema),
	}),
);
export type ItemsProfitMetrics = z.infer<typeof ItemsProfitMetricsSchema>;

export const ItemPerformanceSchema = z.object({
	itemId: z.number(),
	salesVolume: z.number(),
	salesVelocity: z.number(),
	avgCraftCost: z.number(),
	avgListingPrice: z.number(),
	avgDailyProfitMargin: z.number(),
	profitMarginTrend: z.number(),
	craftCostTrend: z.number(),
	priceTrend: z.number(),
	salesTrend: z.number(),
	listingsTrend: z.number(),
	roiTrend: z.number(),
	avgSoldDuration: z.number(),
	craftCostPctChangeFromAvg: z.number(),
});

export type ItemPerformance = z.infer<typeof ItemPerformanceSchema>;

export const DailySalesSchema = z.object({
	date: z.string(),
	sold: z.number(),
	added: z.number(),
	expired: z.number(),
	avgSoldDuration: z.number(),
	listingCount: z.number(),
	revenue: z.number(),
});

export type ItemDailySales = z.infer<typeof DailySalesSchema>;

export const DailySalesListSchema = z.object({
	itemId: z.number(),
	dailySales: z.array(DailySalesSchema),
});

export const CreateGearsetSchema = z.object({
	title: z.string().min(1).max(60),
	characterClass: CharacterClassNameSchema,
	characterGender: z.string().regex(/^[mf]$/),
	tags: z.array(z.string().max(15)).min(1).max(10),
});

export const UpdateGearsetSchema = z.object({
	title: z.string().min(1).optional(),
	characterClass: z.optional(CharacterClassNameSchema),
	characterGender: z
		.string()
		.regex(/^[mf]$/)
		.optional(),
	tags: z.array(z.string().max(15)).min(1).max(10).optional(),
});

export const EquipItemRequestSchema = z.object({
	slotIdentifier: GearsetSlotTypeIdentifierSchema,
	itemId: z.number(),
});

export type EquipItemRequest = z.infer<typeof EquipItemRequestSchema>;

export const LoginRequestSchema = z.object({
	email: z.string().email(),
	password: z.string().min(8),
});
export type LoginRequest = z.infer<typeof LoginRequestSchema>;

export const RegisterRequestSchema = z.object({
	email: z.string().min(1),
	password: z.string().min(8),
});

export type RegisterRequest = z.infer<typeof RegisterRequestSchema>;

export const ItemSearchParamsSchema = z.object({
	name: z.string().min(0).max(100).optional(),
	typesIds: z.string().optional(),
	effectsIds: z.string().optional(),
	itemIds: z.string().optional(),
	ingredient: z.number().optional(),
	minLevel: z.number().min(1).max(200).optional(),
	maxLevel: z.number().min(1).max(200).optional(),
});

export const PageableParamsSchema = z.object({
	page: z.number().min(0).optional(),
	size: z.number().min(1).optional(),
	sort: z.string().optional(),
});

export type PageableParams = z.infer<typeof PageableParamsSchema>;

export const ItemDetailsParamsSchema =
	ItemSearchParamsSchema.merge(PageableParamsSchema);

export type ItemDetailsParams = z.infer<typeof ItemDetailsParamsSchema>;

export type ItemFilters = Omit<
	ItemDetailsParams,
	"typesIds" | "effectsIds" | "itemIds"
> & {
	typesIds?: number[];
	effectsIds?: number[];
	itemIds?: number[];
};

export const DateRangeSchema = z.object({
	startDate: z.string(),
	endDate: z.string(),
});

export type DateRange = z.infer<typeof DateRangeSchema>;

export const ItemMetricsParamsSchema =
	ItemSearchParamsSchema.merge(DateRangeSchema);

export const IdPathParam = z.object({
	id: z.number(),
});

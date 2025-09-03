import type {EffectDto, ItemEffectDto} from "@/api/model";
import {effectIconDirectory} from "@/utils/constants.ts";

export const getEffectIconPath = (normalizedName: string): string => {
  if (!normalizedName) {
    return "";
  }

  return `${effectIconDirectory}${normalizedName}.png`;
};

type SimpleEffectMap = Record<string, string>;

export function getEffectNameFromTemplate(template: string | null): string {
  if (!template) {
    return "";
  }

  let cleanedName = template.replace(/#[0-9]+|{~1~2 à ?([+-]?)}/g, "");

  cleanedName = cleanedName.normalize("NFD").replace(/[\u0300-\u036f]/g, "");

  const normalizedName = cleanedName
  .toLowerCase()
  .trim()
  .replace(/^[+-]\s*/, "")
  .replace(/[()]/g, "")
  .replace(/[\s%-]+/g, "-")
  .replace(/[^a-z0-9-]/g, "")
  .replace(/^-+|-+$/g, "");

  return normalizedName;
}

export function formatItemEffect(
    effectOrMap: ItemEffectDto | SimpleEffectMap,
    allEffectTemplates: EffectDto[],
): { formattedEffect: string; name: string; isPositive: boolean } | null {
  let effectTemplate: EffectDto | undefined;
  let effectValue: string | number | undefined;
  let showRange: boolean = false;
  let isPositive: boolean = false;

  if (
      "effectId" in effectOrMap &&
      ("minValue" in effectOrMap || "maxValue" in effectOrMap)
  ) {
    const {effectId, minValue, maxValue} = effectOrMap;
    effectTemplate = allEffectTemplates.find((t) => t.id === effectId);

    showRange =
        typeof minValue === "number" &&
        typeof maxValue === "number" &&
        maxValue !== 0 &&
        minValue !== maxValue;

    effectValue =
        typeof minValue === "number"
            ? minValue.toString()
            : typeof minValue === "string"
                ? minValue
                : "";
  } else {
    const entries = Object.entries(effectOrMap as SimpleEffectMap);
    if (entries.length === 0) return null;

    const [effectIdStr, value] = entries[0];
    const effectId = Number(effectIdStr);
    effectTemplate = allEffectTemplates.find((t) => t.id === effectId);
    effectValue = value;
  }

  const templateWithoutPlaceholders = effectTemplate?.description_template
  ?.replace(/#[0-9]+|{~1~2 à ?([+-]?)}/g, "")
  .trim();

  if (
      !effectTemplate ||
      !effectTemplate.description_template ||
      effectValue === undefined ||
      !templateWithoutPlaceholders
  ) {
    return null;
  }

  const val1 = effectValue.toString();
  const val2 = showRange
      ? (effectOrMap as ItemEffectDto).maxValue?.toString() || ""
      : "";

  const formattedEffect = effectTemplate.description_template
  .replace(/{~1~2 à ?([+-]?)}/g, showRange ? ` à ` : "")
  .replace(/#1/g, val1)
  .replace(/#2/g, val2)
  .replace(/#[0-9]+/g, "")
  .replace(/%[0-9]+/g, "")
  .trim();

  const effectName = getEffectNameFromTemplate(
      effectTemplate.description_template,
  );

  isPositive = !effectTemplate.description_template.includes("-");

  return {
    name: effectName,
    formattedEffect,
    isPositive,
  };
}

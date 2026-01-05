import type {Effect, ItemEffect} from "@/services/api/api.types";
import {effectIconDirectory} from "@/utils/constants";

export type EffectRow = {
  effectId: number;
  text: string;
  effectSlug: string;
};

export type ItemStatRow = EffectRow & {
  isPositive: boolean;
};

export const itemEffects = new Set([
  "agilite",
  "chance",
  "coups-critiques",
  "dommages-air",
  "dommages-arme",
  "dommages-critiques",
  "dommages-distance",
  "dommages-eau",
  "dommages-feu",
  "dommages-melee",
  "dommages-neutre",
  "dommages-piege",
  "dommages-poussee",
  "dommages-sort",
  "dommages-terre",
  "dommages",
  "esquive-pa",
  "esquive-pm",
  "force",
  "fuite",
  "initiative",
  "intelligence",
  "invocations",
  "pa",
  "pm",
  "po",
  "pods",
  "prospection",
  "puipiege",
  "puissance",
  "pv-rendus",
  "resistance-air",
  "resistance-arme",
  "resistance-critiques",
  "resistance-distance",
  "resistance-dommages",
  "resistance-eau",
  "resistance-feu",
  "resistance-melee",
  "resistance-neutre",
  "retrait-pm",
  "resistance-poussee",
  "resistance-terre",
  "retrait-pa",
  "sagesse",
  "soins",
  "tacle",
  "vitalite",
  "vol-air",
  "vol-eau",
  "vol-feu",
  "vol-neutre",
  "vol-terre",
]);

export const getEffectIconPath = (effectIconName: string): string => {
  if (!effectIconName) {
    return "";
  }
  return `${effectIconDirectory}${effectIconName}.png`;
};

export function slugifyEffectForIcon(template: string | null): string {
  if (!template) return "";

  const slug = template
  .replace(/#[0-9]+|{~1~2 à ?([+-]?)}/g, "")
  .normalize("NFD")
  .replace(/[\u0300-\u036f]/g, "")
  .toLowerCase()
  .trim()
  .replace(/^[+-]\s*/, "")
  .replace(/[()]/g, "")
  .replace(/[\s%-]+/g, "-")
  .replace(/[^a-z0-9-]/g, "")
  .replace(/^-+|-+$/g, "");

  return itemEffects.has(slug) ? slug : "";
}

export function toEffectRow(effectTemplate: Effect): EffectRow | null {
  if (!effectTemplate || !effectTemplate.descriptionTemplate) {
    return null;
  }

  const formattedEffect = effectTemplate.descriptionTemplate
  .replace(/{~1~2 à ?([+-]?)}/g, "")
  .replace(/[#%]\d+/g, "")
  .replace(/^[+-]?\s*0+/, "")
  .trim();

  return {
    effectId: effectTemplate.id,
    text: formattedEffect,
    effectSlug: slugifyEffectForIcon(effectTemplate.descriptionTemplate),
  };
}

export function toItemStatRow(
    effect: ItemEffect,
    allTemplates: Effect[],
): ItemStatRow | null {
  const {effectId, minValue, maxValue} = effect;

  const template = allTemplates.find((t) => t.id === effectId);
  if (!template?.descriptionTemplate) return null;

  const showRange = maxValue !== 0 && minValue !== maxValue;

  const minVal = minValue?.toString() ?? "";
  const maxVal = showRange ? (maxValue?.toString() ?? "") : "";

  const formattedEffect = template.descriptionTemplate
  .replace(/{~1~2 à ?([+-]?)}/g, showRange ? " à " : "")
  .replace(/#1/g, minVal)
  .replace(/#2/g, maxVal)
  .replace(/#[0-9]+/g, "")
  .replace(/%[0-9]+/g, "")
  .trim();

  return {
    effectId,
    effectSlug: slugifyEffectForIcon(template.descriptionTemplate),
    text: formattedEffect,
    isPositive: !template.descriptionTemplate.includes("-"),
  };
}

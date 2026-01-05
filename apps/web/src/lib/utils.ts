import {type ClassValue, clsx} from "clsx";
import {twMerge} from "tailwind-merge";
import type {TimeRange} from "@/types/types.ts";

export function cn(...inputs: ClassValue[]) {
	return twMerge(clsx(inputs));
}

export function getRangeStartDate(range: TimeRange): Date {
  if (range === "max") return new Date(0);

  const d = new Date();
  const days = range === "7d" ? 7 : range === "30d" ? 30 : 365;

  d.setDate(d.getDate() - days);
  d.setHours(0, 0, 0, 0);

  return d;
}

export const formatNumber = (value: number): string => {
  const absValue = Math.abs(value);
  const sign = value < 0 ? "-" : "";

  return absValue >= 1_000_000
      ? `${sign}${(absValue / 1_000_000).toFixed(1)}M`
      : absValue >= 1_000
          ? `${sign}${Math.round(absValue / 1_000)}K`
          : value.toString();
};

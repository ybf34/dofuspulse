import {useGetAllEffects} from "@/api/api.ts";
import type {ItemEffectDto} from "@/api/model";
import {formatItemEffect, getEffectIconPath} from "@/utils/item-effect";

interface ItemStatsProps {
  stats?: ItemEffectDto[];
}

const getEffectColorClass = (isPositive?: boolean) => {
  if (isPositive === true) return "text-green-400";
  if (isPositive === false) return "text-red-400";
  return "text-foreground";
};

export function ItemEffectsList({stats}: ItemStatsProps) {
  const {data: allEffectTemplates, isPending, error} = useGetAllEffects();

  if (isPending) {
    return <p>Loading effects...</p>;
  }

  if (error) {
    return <p>Error loading effects.</p>;
  }

  const formattedStats = stats
  ?.map((stat) => {
    const formatted = formatItemEffect(stat, allEffectTemplates);
    if (!formatted) {
      return null;
    }

    const iconPath = getEffectIconPath(formatted.name);

    return {
      ...formatted,
      iconPath,
    };
  })
  .filter(
      (
          stat,
      ): stat is {
        iconPath: string;
        formattedEffect: string;
        name: string;
        isPositive: boolean;
      } => stat !== null,
  );

  return (
      <div className="flex flex-col gap-0.5 py-1">
        {formattedStats?.map((stat) => (
            <div
                key={stat.name}
                className={`flex items-center ${getEffectColorClass(stat.isPositive)}`}
            >
              <img
                  src={stat.iconPath}
                  alt={`${stat.name} icon`}
                  className="h-5 w-5 mr-1"
                  onError={(e) => {
                    e.currentTarget.style.display = "none";
                  }}
              />
              <span>{stat.formattedEffect}</span>
            </div>
        ))}
      </div>
  );
}

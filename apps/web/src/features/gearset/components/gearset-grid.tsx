import GearsetSlot from "@/features/gearset/components/gearset-slot";
import {useGearsetEditorStore} from "@/features/gearset/hooks/use-gearset-editor-store.ts";
import useGearsetSlotTypesQuery from "@/features/gearset/hooks/use-gearset-slot-types-query.ts";
import useUnequipItemMutation from "@/features/gearset/hooks/use-unequip-item-mutation.ts";
import {getSlotIdentifierTypeIds, SlotSizes,} from "@/features/gearset/utils/gearset.utils.ts";
import {useItemFiltersStore} from "@/features/items/hooks/use-item-filters-store.ts";
import {useItemsPerformanceQuery} from "@/features/metrics/hooks/use-item-performance-queries.ts";
import {useIsMobile} from "@/hooks/use-mobile.ts";
import {getRangeStartDate} from "@/lib/utils.ts";
import {
  type APIGearset,
  type GearsetSlotTypeIdentifier,
  gearsetSlotTypeIdentifiers,
} from "@/services/api/api.types";
import {characterSkinDirectory} from "@/utils/constants";

export type GearsetGridProps = {
  gearset: APIGearset;
  isPreview: boolean;
};

export default function GearsetGrid({gearset, isPreview}: GearsetGridProps) {
  const {id, slots, characterClass, characterGender} = gearset;

  const isMobile = useIsMobile();

  const setFilters = useItemFiltersStore((state) => state.setFilters);
  const setSelectedSlot = useGearsetEditorStore(
      (state) => state.setSelectedSlot,
  );
  const setItemExplorerOpen = useGearsetEditorStore(
      (state) => state.setItemExplorerOpen,
  );

  const {slotTypes} = useGearsetSlotTypesQuery();
  const {unequipItem} = useUnequipItemMutation();
  const {itemsPerformance} = useItemsPerformanceQuery(
      {
        itemIds: slots?.map((slot) => slot.itemDetails.id),
      },
      {
        startDate: getRangeStartDate("7d").toISOString().split("T")[0],
        endDate: new Date().toISOString().split("T")[0],
      },
  );

  const slotSize = isPreview || isMobile ? SlotSizes.small : SlotSizes.large;

  const handleSlotSelect = (
      slotType: GearsetSlotTypeIdentifier,
      showItemExplorerSheet?: boolean,
  ) => {
    setSelectedSlot(slotType);
    if (!slotTypes) return;
    setFilters({typesIds: getSlotIdentifierTypeIds(slotTypes, slotType)});
    if (isMobile && showItemExplorerSheet) setItemExplorerOpen(true);
  };

  const handleUnequipItem = (slotId: number) => {
    unequipItem({
      params: {
        gearSetId: id,
        slotId: slotId,
      },
    });
  };

  return (
      <div className="mx-auto w-fit">
        <div
            className="grid gap-1"
            style={{
              gridTemplateColumns: `repeat(6, ${slotSize.width})`,
              gridTemplateRows: `repeat(5, ${slotSize.height})`,
            }}
        >
          <div className="relative col-span-2 col-start-3 row-start-1 row-span-3">
            <img
                src={`${characterSkinDirectory + characterClass.name.toLowerCase()}-${characterGender}.png`}
                alt="Character Skin"
                style={{
                  width: `calc(${slotSize.width} * 2)`,
                  height: `calc(${slotSize.height} * 3)`,
                }}
                className="object-contain mx-auto"
            />
          </div>

          {Object.values(gearsetSlotTypeIdentifiers).map(
              (slotType: GearsetSlotTypeIdentifier) => {
                const slotWithItem = slots.find(
                    (slot) => slot.slotType?.name === slotType,
                );
                const equippedItemPerformance = itemsPerformance.find(
                    (item) => item.itemId === slotWithItem?.itemDetails.id,
                );
                return (
                    <GearsetSlot
                        isPreview={isPreview}
                        key={gearset.id + slotType}
                        performance={equippedItemPerformance}
                        slotType={slotType}
                        slot={slotWithItem}
                        onSelectSlot={
                          !isPreview
                              ? (slotType, showSheet) =>
                                  handleSlotSelect(slotType, showSheet)
                              : () => {
                              }
                        }
                        onUnequip={(slotId) => handleUnequipItem(slotId)}
                        slotSize={slotSize}
                    />
                );
              },
          )}
        </div>
      </div>
  );
}

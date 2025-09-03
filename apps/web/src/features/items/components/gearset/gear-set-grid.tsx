import GearSetSlotCell from "@/features/items/components/gearset/gear-set-slot-cell.tsx";
import {
  type GearSetGridProps,
  type GearsetSlotIdentifier,
  GearsetSlotIdentifierValues,
} from "@/types/types.ts";
import {characterSkinDirectory} from "@/utils/constants.ts";

export default function GearSetGrid({
                                      id,
                                      characterClass,
                                      gearsetSlots,
                                    }: GearSetGridProps) {
  const allSlots = Object.values(GearsetSlotIdentifierValues);
  return (
      <div className="mx-auto w-fit">
        <div
            id={id}
            className="grid gap-1"
            style={{
              gridTemplateColumns: `repeat(6, 40px)`,
              gridTemplateRows: `repeat(5, 36px)`,
            }}
        >
          <img
              src={
                  characterSkinDirectory +
                  characterClass.name +
                  "-" +
                  characterClass.gender +
                  ".png"
              }
              alt="Character Skin"
              className="col-span-2 col-start-3 row-start-1 row-span-3 h-[124px] w-[84px] shrink-0 object-contain"
          />
          {allSlots.map((slotType: GearsetSlotIdentifier) => {
            const slotWithItem = gearsetSlots.find(
                (slot) => slot.slotType?.name === slotType,
            );
            return (
                <GearSetSlotCell
                    key={slotType}
                    itemDetails={slotWithItem?.itemDetails}
                    slotIdentifier={slotType}
                />
            );
          })}
        </div>
      </div>
  );
}

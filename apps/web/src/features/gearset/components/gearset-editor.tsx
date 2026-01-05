import {Search} from "lucide-react";
import {Button} from "@/components/ui/button";
import GearsetEditorTopbar
  from "@/features/gearset/components/gearset-editor/gearset-editor-topbar.tsx";
import GearsetGrid from "@/features/gearset/components/gearset-grid";
import {useGearsetEditorStore} from "@/features/gearset/hooks/use-gearset-editor-store.ts";
import {useIsMobile} from "@/hooks/use-mobile.ts";
import type {APIGearset} from "@/services/api/api.types.ts";

export type GearsetEditorProps = {
  gearset: APIGearset;
};

export default function GearsetEditor({gearset}: GearsetEditorProps) {
  const isMobile = useIsMobile();

  const setItemExplorerOpen = useGearsetEditorStore(
      (state) => state.setItemExplorerOpen,
  );
  return (
      <div className="flex flex-1 flex-col h-full overflow-hidden">
        <GearsetEditorTopbar gearset={gearset}/>
        {isMobile && (
            <div className="px-6 pt-8 pb-4">
              <Button
                  className="w-full"
                  variant="outline"
                  onClick={() => setItemExplorerOpen(true)}
              >
                <Search className="h-4 w-4 mr-3"/>
                Search Items
              </Button>
            </div>
        )}
        <div
            className="
          flex flex-1 flex-col p-4
          bg-[radial-gradient(circle_at_50%_35%,rgba(0,0,0,0.015)_1px,transparent_1px)]
          [background-size:8px_8px]
          dark:bg-[repeating-linear-gradient(45deg,rgba(255,255,255,0.015)_0px,rgba(255,255,255,0.015)_1px,transparent_1px,transparent_12px)]
          dark:[background-size:auto]
        "
        >
          <div className="flex flex-1 justify-center pt-[8vh]">
            <GearsetGrid gearset={gearset} isPreview={false}/>
          </div>
        </div>
      </div>
  );
}

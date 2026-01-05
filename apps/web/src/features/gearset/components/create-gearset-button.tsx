import {Gamepad2, Plus} from "lucide-react";
import {useState} from "react";
import {Button} from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import GearsetForm from "@/features/gearset/components/gearset-form";

export default function CreateGearsetButton() {
  const [open, setOpen] = useState(false);

  return (
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogTrigger asChild>
          <Button size="sm" className="gap-2 border transition-all duration-200">
            <Plus className="h-4 w-4"/>
            New Gearset
          </Button>
        </DialogTrigger>
        <DialogContent
            className="w-full max-w-md border bg-card dark:bg-zinc-950 text-card-foreground dark:text-white p-4">
          <DialogHeader className="pb-3">
            <div className="flex items-center gap-2">
              <Gamepad2 className="h-4 w-4 text-muted-foreground"/>
              <div>
                <DialogTitle className="text-lg font-semibold text-foreground dark:text-white">
                  Create New Gearset
                </DialogTitle>
                <DialogDescription
                    className="text-xs text-muted-foreground dark:text-gray-400 mt-0.5">
                  Craft your perfect build configuration
                </DialogDescription>
              </div>
            </div>
          </DialogHeader>
          <GearsetForm onSuccess={() => setOpen(false)}/>
        </DialogContent>
      </Dialog>
  );
}

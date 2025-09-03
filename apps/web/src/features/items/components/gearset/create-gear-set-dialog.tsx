import {zodResolver} from "@hookform/resolvers/zod";
import {Gamepad2, Plus, Sparkles} from "lucide-react";
import {useState} from "react";
import {useForm} from "react-hook-form";
import {toast} from "sonner";
import {z} from "zod";
import {useCreateGearSet} from "@/api/api";
import {Button} from "@/components/ui/button";
import CharacterClassSelect from "@/components/ui/character-class-select";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import {Input} from "@/components/ui/input";
import {TagSelector} from "@/components/ui/tag-selector";
import {characterClassNames} from "@/utils/constants";

export const createGearsetSchema = z.object({
  title: z.string().trim().min(1, "Title is required").max(60, "Max 60 chars"),
  characterClass: z.string().min(1, "Pick a class"),
  characterGender: z.enum(["m", "f"]),
  tags: z
  .array(z.string().trim())
  .min(1, "At least 1 tag required")
  .max(5, "Max 5 tags allowed"),
});

type FormSchema = z.infer<typeof createGearsetSchema>;

const DEFAULTS: FormSchema = {
  title: "",
  characterClass: characterClassNames.at(0) ?? "",
  characterGender: "m",
  tags: [],
};

export default function CreateGearsetDialog() {
  const [open, setOpen] = useState(false);

  const form = useForm<FormSchema>({
    resolver: zodResolver(createGearsetSchema),
    defaultValues: DEFAULTS,
  });

  const {mutate: createGearSet, isPending} = useCreateGearSet({
    mutation: {
      onSuccess: () => {
        form.reset(DEFAULTS);
        setOpen(false);
        toast.success("Gearset created!", {duration: 2500});
      },
    },
  });

  const onSubmit = form.handleSubmit((values) => {
    createGearSet({data: values});
  });

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

          <Form {...form}>
            <form onSubmit={onSubmit} className="space-y-4">
              <FormField
                  control={form.control}
                  name="title"
                  render={({field}) => (
                      <FormItem className="space-y-2">
                        <FormLabel
                            className="text-sm font-medium text-foreground dark:text-white flex items-center gap-2">
                          Gearset Name
                          <span
                              className="text-xs text-muted-foreground dark:text-gray-400 font-normal">
											({field.value?.length || 0}/60)
										</span>
                        </FormLabel>
                        <Input
                            placeholder="Enter a memorable name for your gearset..."
                            maxLength={60}
                            className="h-9 px-3 bg-background dark:bg-zinc-950 border dark:border-zinc-700 text-foreground dark:text-white placeholder:text-muted-foreground dark:placeholder:text-gray-500 focus:border-ring focus:ring-1 focus:ring-ring transition-colors"
                            {...field}
                        />
                        <FormMessage className="text-xs"/>
                      </FormItem>
                  )}
              />

              <FormField
                  control={form.control}
                  name="tags"
                  render={({field}) => (
                      <FormItem className="space-y-2">
                        <FormLabel
                            className="text-sm font-medium text-foreground dark:text-white flex items-center gap-2">
                          Tags
                          <span
                              className="text-xs text-muted-foreground dark:text-gray-400 font-normal"></span>
                        </FormLabel>
                        <div
                            className="p-3 rounded-lg border dark:border-zinc-700 bg-muted/50 dark:bg-zinc-950/50">
                          <TagSelector
                              value={field.value ?? []}
                              onChange={field.onChange}
                              max={5}
                              disabled={isPending}
                          />
                        </div>
                        <FormMessage className="text-xs"/>
                      </FormItem>
                  )}
              />

              <FormItem className="space-y-2">
                <FormLabel className="text-sm font-medium text-foreground dark:text-white">
                  Select a class
                </FormLabel>
                <div
                    className="p-3 rounded-lg border dark:border-zinc-700 bg-muted/50 dark:bg-zinc-950/50">
                  <CharacterClassSelect
                      classValue={
                          form.watch("characterClass") ?? characterClassNames[0]
                      }
                      genderValue={
                          (form.watch("characterGender") as "m" | "f") ?? "m"
                      }
                      onClassChange={(v) => form.setValue("characterClass", v)}
                      onGenderChange={(g) => form.setValue("characterGender", g)}
                  />
                </div>
                <FormMessage className="text-xs"/>
              </FormItem>

              <div
                  className="flex items-center justify-end gap-3 pt-4 border-t dark:border-zinc-700">
                <Button
                    type="button"
                    variant="ghost"
                    onClick={() => setOpen(false)}
                    className="px-4 text-muted-foreground dark:text-gray-400 hover:text-foreground dark:hover:text-white transition-colors"
                >
                  Cancel
                </Button>
                <Button
                    type="submit"
                    disabled={isPending}
                    className="px-6 min-w-[100px] dark:bg-white dark:text-black dark:hover:bg-gray-200"
                >
                  {isPending ? (
                      <div className="flex items-center gap-2">
                        <div
                            className="w-3 h-3 border-2 border-current/30 border-t-current rounded-full animate-spin"/>
                        Creating...
                      </div>
                  ) : (
                      <div className="flex items-center gap-2">
                        <Sparkles className="h-3 w-3"/>
                        Create Gearset
                      </div>
                  )}
                </Button>
              </div>
            </form>
          </Form>
        </DialogContent>
      </Dialog>
  );
}

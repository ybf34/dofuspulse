import {zodResolver} from "@hookform/resolvers/zod";
import {useNavigate} from "@tanstack/react-router";
import {Sparkles} from "lucide-react";
import {useForm} from "react-hook-form";
import {z} from "zod";
import {Button} from "@/components/ui/button";
import CharacterClassSelect from "@/components/ui/character-class-select";
import {Form, FormField, FormItem, FormLabel, FormMessage,} from "@/components/ui/form";
import {Input} from "@/components/ui/input";
import {TagSelector} from "@/components/ui/tag-selector";
import useCreateGearsetMutation from "@/features/gearset/hooks/use-create-gearset-mutation";
import {CharacterClassNameSchema} from "@/services/api/api.types";
import type {CharacterClassType} from "@/types/types";

type GearSetFormProp = {
  onSuccess: () => void;
};

export const CharacterSchema: z.ZodType<CharacterClassType> = z.object({
  characterClass: CharacterClassNameSchema,
  gender: z.enum(["m", "f"]),
});

export const createGearsetSchema = z.object({
  title: z.string().trim().min(1, "Title is required").max(60, "Max 60 chars"),
  character: CharacterSchema,
  tags: z
  .array(z.string().trim())
  .min(1, "At least 1 tag required")
  .max(5, "Max 5 tags allowed"),
});

export default function GearsetForm({onSuccess}: GearSetFormProp) {
  type FormSchema = z.infer<typeof createGearsetSchema>;

  const navigate = useNavigate();
  const {createGearset, isPending} = useCreateGearsetMutation();

  const DEFAULTS: FormSchema = {
    title: "",
    character: {
      characterClass: "CRA",
      gender: "m",
    },
    tags: [],
  };

  const form = useForm<FormSchema>({
    resolver: zodResolver(createGearsetSchema),
    defaultValues: DEFAULTS,
  });

  const onSubmit = form.handleSubmit(async (data) => {
    const newGearset = await createGearset({
      body: {
        ...data,
        characterClass: data.character.characterClass,
        characterGender: data.character.gender,
      },
    });
    onSuccess();
    await navigate({to: `/gearsets/${newGearset.body.id}`});
  });

  return (
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
                  value={form.watch("character")}
                  onChange={(v) => form.setValue("character", v)}
              />
            </div>
            <FormMessage className="text-xs"/>
          </FormItem>

          <div className="flex items-center justify-end gap-3 pt-4 border-t dark:border-zinc-700">
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
  );
}

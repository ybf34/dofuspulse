import {Mars, Pencil, Venus} from "lucide-react";
import {Badge} from "@/components/ui/badge";
import {Button} from "@/components/ui/button";
import CharacterClassAvatar from "@/components/ui/character-class-avatar";
import {getClassAvatarPath} from "@/features/gearset/utils/gearset.utils";
import type {CharacterClassName} from "@/services/api/api.types.ts";

interface CharacterSectionProps {
  characterClass: CharacterClassName;
  gender: string;
  onEdit: () => void;
}

export function CharacterSection({
                                   characterClass,
                                   gender,
                                   onEdit,
                                 }: CharacterSectionProps) {
  const isMale = gender.toLowerCase() === "m";

  return (
      <div className="flex items-center gap-2">
        <CharacterClassAvatar
            imageSrc={getClassAvatarPath(characterClass, gender)}
            altText={characterClass}
            isSelected
            size={32}
        />
        <Badge
            variant="outline"
            className={`h-6 px-2.5 text-xs inline-flex items-center gap-1.5 ${
                isMale
                    ? "border-blue-400/40 bg-blue-500/10 text-blue-600 dark:text-blue-400"
                    : "border-pink-400/40 bg-pink-500/10 text-pink-600 dark:text-pink-400"
            }`}
            title={isMale ? "Male" : "Female"}
        >
          {isMale ? (
              <Mars className="h-3.5 w-3.5"/>
          ) : (
              <Venus className="h-3.5 w-3.5"/>
          )}
          {gender.toUpperCase()}
        </Badge>
        <Button
            variant="ghost"
            size="icon"
            onClick={onEdit}
            className="h-7 w-7 text-muted-foreground hover:text-foreground"
            aria-label="Edit character"
        >
          <Pencil className="h-3.5 w-3.5"/>
        </Button>
      </div>
  );
}

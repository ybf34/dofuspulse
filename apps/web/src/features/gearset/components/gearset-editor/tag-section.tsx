import {Check, Plus, X} from "lucide-react";
import {useState} from "react";
import {toast} from "sonner";
import {Badge} from "@/components/ui/badge";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {MAX_GEARSET_TAG_LENGTH} from "@/features/gearset/utils/gearset.utils";

interface TagsSectionProps {
  tags: string[];
  isFull: boolean;
  onAddTag: (tag: string) => Promise<boolean>;
  onRemoveTag: (tag: string) => void;
}

export function TagsSection({
                              tags,
                              isFull,
                              onAddTag,
                              onRemoveTag,
                            }: TagsSectionProps) {
  const [isAdding, setIsAdding] = useState(false);
  const [newTag, setNewTag] = useState("");
  const [error, setError] = useState(false);

  const addTag = async () => {
    const trimmed = newTag.trim();
    if (!trimmed) return;
    if (tags.map((t) => t.toLowerCase()).includes(trimmed.toLowerCase())) {
      toast.error(`Tag "${trimmed}" already exists.`);
      return;
    }
    const success = await onAddTag(trimmed);

    if (success) {
      setNewTag("");
      setIsAdding(false);
    } else {
      setError(true);
    }
  };

  return (
      <div className="flex flex-wrap items-center gap-2">
        {tags.map((tag) => (
            <Badge
                key={tag}
                variant="outline"
                className="group h-7 px-2 pr-1 text-xs font-medium bg-muted/40 border-border/60 text-foreground/80 hover:bg-muted/60 transition-all"
            >
              {tag}
              <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => onRemoveTag(tag)}
                  className="ml-1 h-5 w-5 opacity-0 group-hover:opacity-100 hover:bg-destructive/10 transition-opacity"
              >
                <X className="h-3 w-3 text-destructive"/>
              </Button>
            </Badge>
        ))}

        {!isFull &&
            (isAdding ? (
                <div className="flex items-center gap-1">
                  <Input
                      value={newTag}
                      onChange={(e) =>
                          setNewTag(e.target.value.slice(0, MAX_GEARSET_TAG_LENGTH))
                      }
                      placeholder="Tag..."
                      className={`h-7 w-32 text-xs transition-colors ${error ? "border-destructive ring-destructive focus-visible:ring-destructive" : ""}`}
                      autoFocus
                      onKeyDown={(e) => {
                        if (e.key === "Enter") addTag();
                        if (e.key === "Escape") setIsAdding(false);
                      }}
                  />
                  <Button
                      variant="ghost"
                      size="icon"
                      onClick={addTag}
                      className="h-7 w-7"
                  >
                    <Check className="h-3.5 w-3.5 text-green-600"/>
                  </Button>
                  <Button
                      variant="ghost"
                      size="icon"
                      onClick={() => setIsAdding(false)}
                      className="h-7 w-7"
                  >
                    <X className="h-3.5 w-3.5 text-destructive"/>
                  </Button>
                </div>
            ) : (
                <Button
                    variant="outline"
                    size="icon"
                    onClick={() => setIsAdding(true)}
                    className="h-7 border-dashed"
                >
                  <Plus className="h-3.5 w-3.5"/>
                </Button>
            ))}
      </div>
  );
}

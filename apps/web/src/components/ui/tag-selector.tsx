import {X} from "lucide-react";
import {useRef} from "react";
import {Badge} from "@/components/ui/badge";
import {FormControl} from "@/components/ui/form";

type TagSelectorProps = {
  value: string[];
  onChange: (tags: string[]) => void;
  max?: number;
  disabled?: boolean;
  placeholder?: string;
};

export function TagSelector({
                              value,
                              onChange,
                              max = 5,
                              disabled,
                              placeholder = "Type a tag and press Enter",
                            }: TagSelectorProps) {
  const inputRef = useRef<HTMLInputElement | null>(null);
  const limitReached = value.length >= max;

  const addTag = (tag: string) => {
    const clean = tag.trim().toLowerCase();
    if (!clean) return;
    if (value.includes(clean)) return;
    if (limitReached) return;
    onChange([...value, clean]);
    if (inputRef.current) inputRef.current.value = "";
  };

  const removeTag = (tag: string) => {
    onChange(value.filter((t) => t !== tag));
  };

  const handleKeyDown: React.KeyboardEventHandler<HTMLInputElement> = (e) => {
    if (e.key === "Enter" || e.key === ",") {
      e.preventDefault();
      addTag((e.target as HTMLInputElement).value);
    } else if (
        e.key === "Backspace" &&
        (e.target as HTMLInputElement).value === ""
    ) {
      const tags = [...value];
      if (tags.length > 0) removeTag(tags[tags.length - 1]);
    }
  };

  return (
      <FormControl>
        <div className="space-y-1">
          <div
              className={`flex flex-wrap items-center gap-2 rounded-md border px-2 py-1 ${
                  limitReached
                      ? "border-red-500 focus-within:ring-red-500"
                      : "border-input"
              }`}
          >
            {value?.map((t) => (
                <Badge key={t} variant="secondary" className="px-2 py-1 text-xs">
                  {t}
                  <button
                      type="button"
                      onClick={() => removeTag(t)}
                      className="ml-1"
                      disabled={disabled}
                  >
                    <X className="h-3 w-3"/>
                  </button>
                </Badge>
            ))}
            <input
                ref={inputRef}
                type="text"
                placeholder={
                  limitReached
                      ? `Max ${max} tags`
                      : value.length === 0
                          ? placeholder
                          : ""
                }
                disabled={disabled || limitReached}
                onKeyDown={handleKeyDown}
                className="flex-1 bg-transparent text-sm outline-none placeholder:text-muted-foreground disabled:cursor-not-allowed"
            />
          </div>
          <p
              className={`text-xs ${
                  limitReached ? "text-red-500" : "text-muted-foreground"
              }`}
          >
            {value.length}/{max} tags used
          </p>
        </div>
      </FormControl>
  );
}

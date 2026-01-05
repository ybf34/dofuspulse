import { Check, Pencil, X } from "lucide-react";
import { useState } from "react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { MAX_TITLE_LENGTH } from "@/features/gearset/utils/gearset.utils";

interface TitleSectionProps {
	value: string;
	isSaving?: boolean;
	onSave: (title: string) => void;
	className?: string;
}

export function TitleSection({
	value,
	isSaving,
	onSave,
	className,
}: TitleSectionProps) {
	const [isEditing, setIsEditing] = useState(false);
	const [title, setTitle] = useState(value);

	const handleSave = () => {
		if (!title.trim()) {
			toast.error("Title cannot be empty");
			return;
		}

		if (title !== value) {
			onSave(title);
		}
		setIsEditing(false);
	};

	const handleCancel = () => {
		setTitle(value);
		setIsEditing(false);
	};

	return (
		<>
			<div className={`min-w-0 ${className}`}>
				{isEditing ? (
					<Input
						type="text"
						value={title}
						onChange={(e) =>
							setTitle(e.target.value.slice(0, MAX_TITLE_LENGTH))
						}
						autoFocus
						disabled={isSaving}
						className="w-full min-w-0 truncate text-2xl sm:text-3xl lg:text-4xl font-bold tracking-tight p-0 m-0 dark:bg-background border-none focus-visible:ring-0 focus-visible:ring-offset-0 dark:text-white"
					/>
				) : (
					<h1
						className="truncate overflow-hidden whitespace-nowrap text-ellipsis w-full block min-w-0 text-2xl sm:text-3xl lg:text-4xl font-bold tracking-tight text-foreground"
						title={title}
					>
						{title || "Untitled Build"}
					</h1>
				)}
			</div>

			<div className="flex items-center gap-2 flex-shrink-0">
				<Button
					variant="ghost"
					size="icon"
					onClick={isEditing ? handleSave : () => setIsEditing(true)}
					disabled={isSaving}
					className="h-8 w-8 text-muted-foreground hover:text-foreground hover:bg-muted/60"
					aria-label={isEditing ? "Save title" : "Edit title"}
				>
					{isEditing ? <Check /> : <Pencil />}
				</Button>

				{isEditing && (
					<Button
						variant="ghost"
						size="icon"
						onClick={handleCancel}
						disabled={isSaving}
						className="h-8 w-8 text-muted-foreground hover:text-foreground hover:bg-muted/60"
						aria-label="Cancel editing"
					>
						<X className="h-4 w-4 text-destructive" />
					</Button>
				)}
			</div>
		</>
	);
}

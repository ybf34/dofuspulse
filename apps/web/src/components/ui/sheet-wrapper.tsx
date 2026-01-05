import type { ReactNode } from "react";
import {
	Sheet,
	SheetContent,
	SheetHeader,
	SheetTitle,
	SheetTrigger,
} from "@/components/ui/sheet";

interface SheetWrapperProps {
	children: ReactNode;
	trigger?: ReactNode;
	open?: boolean;
	onOpenChange?: (open: boolean) => void;
	side?: "left" | "right" | "top" | "bottom";
	className?: string;
	ariaLabel: string;
}

export function SheetWrapper({
	children,
	trigger,
	open,
	onOpenChange,
	side = "right",
	className,
	ariaLabel,
}: SheetWrapperProps) {
	return (
		<Sheet open={open} onOpenChange={onOpenChange}>
			{trigger && <SheetTrigger asChild>{trigger}</SheetTrigger>}

			<SheetContent
				side={side}
				className={className}
				aria-describedby={ariaLabel}
			>
				<SheetHeader className="sr-only">
					<SheetTitle>{ariaLabel}</SheetTitle>
				</SheetHeader>

				{children}
			</SheetContent>
		</Sheet>
	);
}

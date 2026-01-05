import { cn } from "@/lib/utils";

interface KamasIconProps {
	className?: string;
}

export function KamasIcon({ className }: KamasIconProps) {
	return (
		<img
			src="/icons/kamas-coin.png"
			alt="K"
			className={cn("h-4 w-4", className)}
		/>
	);
}

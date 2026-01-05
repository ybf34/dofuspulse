import { ArrowDownRight, ArrowUpRight } from "lucide-react";
import {
	Tooltip,
	TooltipContent,
	TooltipProvider,
	TooltipTrigger,
} from "@/components/ui/tooltip";

export interface MarketSignalProps {
	label: string;
	value: number;
	tooltip: string;
}

export function MarketSignalTag({ label, value, tooltip }: MarketSignalProps) {
	const positive = value >= 0;

	return (
		<TooltipProvider>
			<Tooltip>
				<TooltipTrigger asChild>
					<span
						className={`
              inline-flex items-center gap-1 px-2 py-1 text-[11px] uppercase tracking-wide rounded-md border backdrop-blur-sm           
              ${
								positive
									? "text-amber-800 dark:text-amber-400 border-amber-400/50 bg-amber-400/10"
									: "text-red-800 dark:text-red-400 border-red-400/50 bg-red-400/10"
							}
              dark:bg-black/20
            `}
					>
						{positive ? (
							<ArrowUpRight className="w-3 h-3 opacity-60" />
						) : (
							<ArrowDownRight className="w-3 h-3 opacity-60" />
						)}
						{label} {Math.abs(value).toFixed(1)}%
					</span>
				</TooltipTrigger>
				<TooltipContent>
					<p>{tooltip}</p>
				</TooltipContent>
			</Tooltip>
		</TooltipProvider>
	);
}

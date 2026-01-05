import { Button } from "@/components/ui/button";

export type TimeRange = "7d" | "30d" | "1y" | "max";

export function TimeRangeTabs({
	range,
	onChange,
}: {
	range: TimeRange;
	onChange: (next: TimeRange) => void;
}) {
	const ranges: TimeRange[] = ["7d", "30d", "1y", "max"];

	return (
		<div className="flex items-center p-0.5 rounded-md border border-input bg-background w-full">
			{ranges.map((r, index) => {
				const isSelected = range === r;
				const isLast = index === ranges.length - 1;

				return (
					<Button
						key={r}
						onClick={() => onChange(r)}
						className={`
              text-xs px-3 py-1.5 transition duration-150 font-medium h-auto
              flex-1       
              ${isSelected ? "z-10" : "z-0"}
              ${index === 0 ? "rounded-l-sm rounded-r-none" : ""}
              ${isLast ? "rounded-r-sm rounded-l-none" : ""}
              ${index > 0 && !isLast ? "rounded-none" : ""}
              ${!isLast && !isSelected ? "border-r border-input" : ""}
              ${
								isSelected
									? "bg-primary text-primary-foreground shadow-sm hover:bg-primary/90"
									: "bg-transparent text-muted-foreground hover:bg-accent hover:text-accent-foreground"
							}
            `}
					>
						{r === "max" ? "MAX" : r.toUpperCase()}
					</Button>
				);
			})}
		</div>
	);
}

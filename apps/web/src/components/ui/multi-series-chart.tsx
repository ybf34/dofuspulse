import {
	Area,
	AreaChart,
	Bar,
	BarChart,
	CartesianGrid,
	Line,
	LineChart,
	XAxis,
	YAxis,
} from "recharts";
import {
	ChartContainer,
	ChartLegend,
	ChartLegendContent,
	ChartTooltip,
	ChartTooltipContent,
} from "@/components/ui/chart";
import { formatNumber } from "@/lib/utils.ts";

type ChartType = "line" | "bar" | "area";

type Series<TData> = {
	key: keyof TData;
	label?: string;
	color?: string;
};

type ChartProps<TData> = {
	data: TData[];
	xKey: keyof TData;
	series: Series<TData>[];
	type: ChartType;
	minHeight: number;
};

export default function MultiSeriesChart<TData>({
	data,
	xKey,
	series,
	type,
	minHeight,
}: ChartProps<TData>) {
	const ChartComponent =
		type === "bar" ? BarChart : type === "area" ? AreaChart : LineChart;

	return (
		<ChartContainer
			config={{}}
			className="w-full"
			style={{
				height: `${minHeight}px`,
			}}
		>
			<ChartComponent data={data} height={minHeight}>
				<CartesianGrid vertical={false} strokeDasharray="3 3" />
				<XAxis
					dataKey={xKey as string}
					tickLine={false}
					axisLine={false}
					tickMargin={6}
				/>
				<YAxis
					tickLine={false}
					axisLine={false}
					width={50}
					tickCount={6}
					tickFormatter={formatNumber}
				/>
				<ChartTooltip content={<ChartTooltipContent />} />
				<ChartLegend content={<ChartLegendContent />} />

				{series.map((s, i) => {
					const color =
						(s.color ?? series.length > 1)
							? `var(--chart-${i + 1})`
							: `var(--chart-neutral)`;

					if (type === "bar") {
						return (
							<Bar
								key={String(s.key)}
								dataKey={s.key as string}
								fill={color}
								radius={4}
							/>
						);
					}

					if (type === "area") {
						return (
							<Area
								key={String(s.key)}
								dataKey={s.key as string}
								stroke={color}
								fill={color}
								type="monotone"
							/>
						);
					}

					return (
						<Line
							key={String(s.key)}
							dataKey={s.key as string}
							stroke={color}
							dot={false}
							strokeWidth={2}
							type="monotone"
						/>
					);
				})}
			</ChartComponent>
		</ChartContainer>
	);
}

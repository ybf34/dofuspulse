import { useDeleteGearSet, useGetUserGearSets } from "@/api/api.ts";
import type { GearSetDto } from "@/api/model";
import LoadingSpinner from "@/components/ui/loading-spinner.tsx";
import CreateGearsetDialog from "@/features/items/components/gearset/create-gear-set-dialog.tsx";
import GearSetCard from "@/features/items/components/gearset/gear-set-card.tsx";

export default function GearsetsPage() {
	const { data: gearsets, isPending } = useGetUserGearSets();
	const { mutate: deleteGearSet } = useDeleteGearSet();

	if (isPending) {
		return (
			<div className="flex justify-center items-center h-screen">
				<LoadingSpinner className="size-55" />
			</div>
		);
	}

	const hasGearsets = gearsets && gearsets.length > 0;

	return (
		<div className="p-6">
			<div className="flex justify-between items-center mb-6">
				<h2 className="text-3xl font-bold">My Gearsets</h2>
				<CreateGearsetDialog />
			</div>

			{hasGearsets ? (
				<ul className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
					{gearsets.map((gearset: GearSetDto) => (
						<li key={gearset.id}>
							<GearSetCard
								gearSet={gearset}
								onDelete={() => deleteGearSet({ id: Number(gearset.id) })}
							/>
						</li>
					))}
				</ul>
			) : (
				<div className="flex flex-col items-center justify-center py-20">
					<h3 className="text-2xl font-bold text-white">No Gearsets Found</h3>
					<p className="mt-2 text-white">
						You haven't created any gearsets yet.
					</p>
				</div>
			)}
		</div>
	);
}

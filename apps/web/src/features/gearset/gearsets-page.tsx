import {Search, Swords} from "lucide-react";
import {
  Empty,
  EmptyContent,
  EmptyDescription,
  EmptyHeader,
  EmptyMedia,
  EmptyTitle,
} from "@/components/ui/empty";
import {Input} from "@/components/ui/input";
import LoadingSpinner from "@/components/ui/loading-spinner";
import CreateGearsetButton from "@/features/gearset/components/create-gearset-button";
import GearsetCard from "@/features/gearset/components/gearset-card";
import useDeleteGearsetMutation from "@/features/gearset/hooks/use-delete-gearset-mutation";
import {useGearsetsQuery} from "@/features/gearset/hooks/use-gearsets-query";
import type {APIGearset} from "@/services/api/api.types";

export default function GearsetsPage() {
  const {gearsets, isPending} = useGearsetsQuery();
  const {deleteGearset} = useDeleteGearsetMutation();

  if (isPending) {
    return (
        <div className="flex items-center justify-center h-[80vh]">
          <LoadingSpinner className="size-58"/>
        </div>
    );
  }

  const hasGearsets = gearsets && gearsets.length > 0;

  return (
      <div className="min-h-screen">
        <div
            className="border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 sticky top-0 z-10">
          <div className="container mx-auto px-6 py-6">
            <div className="flex items-center justify-between gap-4">
              <div>
                <h1 className="text-3xl font-bold tracking-tight">Gearsets</h1>
                <p className="text-sm text-muted-foreground mt-1">
                  {hasGearsets ? `${gearsets.length} builds` : "No builds yet"}
                </p>
              </div>
              <CreateGearsetButton/>
            </div>

            {hasGearsets && (
                <div className="mt-6">
                  <div className="relative max-w-md">
                    <Search
                        className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground"/>
                    <Input
                        type="text"
                        placeholder="Search builds..."
                        className="pl-9"
                    />
                  </div>
                </div>
            )}
          </div>
        </div>

        <div className="space-y-6 bg-background px-6 py-6">
          {hasGearsets ? (
              <ul className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {gearsets.map((gearset: APIGearset) => (
                    <li key={gearset.id}>
                      <GearsetCard
                          gearset={gearset}
                          onDelete={() =>
                              deleteGearset({
                                params: {id: gearset.id},
                              })
                          }
                      />
                    </li>
                ))}
              </ul>
          ) : (
              <Empty className="py-24">
                <EmptyHeader>
                  <EmptyMedia variant="icon">
                    <Swords className="h-12 w-12"/>
                  </EmptyMedia>
                  <EmptyTitle>No gearsets yet</EmptyTitle>
                  <EmptyDescription>
                    Create your first gearset and start planning your builds
                  </EmptyDescription>
                </EmptyHeader>
                <EmptyContent>
                  <CreateGearsetButton/>
                </EmptyContent>
              </Empty>
          )}
        </div>
      </div>
  );
}

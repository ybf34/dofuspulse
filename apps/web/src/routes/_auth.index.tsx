import { createFileRoute } from "@tanstack/react-router";
import GearsetsPage from "@/features/items/page/gear-sets-page.tsx";

export const Route = createFileRoute("/_auth/")({
	component: Index,
});

function Index() {
	return <GearsetsPage />;
}

import { type ReactNode, useState } from "react";
import {
	AlertDialog,
	AlertDialogAction,
	AlertDialogCancel,
	AlertDialogContent,
	AlertDialogDescription,
	AlertDialogFooter,
	AlertDialogHeader,
	AlertDialogTitle,
	AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";

interface ConfirmAlertDialogProps {
	title: string;
	description: string;
	confirmText?: string;
	onConfirm: () => void;
	children: ReactNode;
}

export default function ConfirmAlertDialog({
	title,
	description,
	confirmText = "Confirm",
	onConfirm,
	children,
}: ConfirmAlertDialogProps) {
	const [isOpen, setIsOpen] = useState(false);

	const handleConfirm = () => {
		onConfirm();
		setIsOpen(false);
	};

	return (
		<AlertDialog open={isOpen} onOpenChange={setIsOpen}>
			<AlertDialogTrigger asChild>{children}</AlertDialogTrigger>
			<AlertDialogContent className="w-full max-w-sm sm:max-w-md rounded-xl p-0">
				<AlertDialogHeader className="space-y-1 px-5 py-4">
					<AlertDialogTitle className="text-lg font-semibold leading-tight">
						{title}
					</AlertDialogTitle>
					<AlertDialogDescription className="text-sm text-gray-400">
						{description}
					</AlertDialogDescription>
				</AlertDialogHeader>
				<AlertDialogFooter className="p-5 flex-row justify-end gap-2">
					<AlertDialogCancel asChild>
						<Button type="button" variant="ghost" size="sm">
							Cancel
						</Button>
					</AlertDialogCancel>
					<AlertDialogAction asChild>
						<Button
							type="button"
							variant="destructive"
							size="sm"
							onClick={handleConfirm}
						>
							{confirmText}
						</Button>
					</AlertDialogAction>
				</AlertDialogFooter>
			</AlertDialogContent>
		</AlertDialog>
	);
}

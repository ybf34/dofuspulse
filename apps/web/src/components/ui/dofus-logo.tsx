import DofusIcon from "@/components/icons/dofus-icon";

export type DofusLogoProps = {
	className?: string;
};

export function DofusLogo({ className }: DofusLogoProps) {
	const style = `m-auto fill-secondary ${className || ""}`;

	return <DofusIcon className={style} />;
}

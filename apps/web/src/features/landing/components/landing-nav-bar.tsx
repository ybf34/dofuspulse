import { useState } from "react";
import DofusIcon from "@/components/icons/dofus-icon.tsx";
import { cn } from "@/lib/utils";

interface LandingNavbarProps {
	onLoginClick: () => void;
}

const NAV_LINKS = [
	{ href: "#features", label: "Features" },
	{ href: "#about", label: "About" },
	{ href: "#preview", label: "Preview" },
];

const LandingNavbar = ({ onLoginClick }: LandingNavbarProps) => {
	const [isExpanded, setIsExpanded] = useState(false);

	const handleToggle = () => setIsExpanded((prev) => !prev);
	const handleClose = () => setIsExpanded(false);

	const handleSignIn = () => {
		handleClose();
		onLoginClick();
	};

	return (
		<>
			<nav className="flex justify-center relative z-50">
				<div className="bg-black rounded-full flex items-center pointer-events-auto border border-zinc-800 px-4 py-4 md:px-10 md:py-4 md:gap-12">
					<div className="hidden md:flex items-center gap-8">
						{NAV_LINKS.slice(0, 2).map((link) => (
							<a
								key={link.href}
								href={link.href}
								className="text-[10px] text-zinc-400 hover:text-white transition-colors uppercase tracking-wider font-medium"
							>
								{link.label}
							</a>
						))}
					</div>

					<button
						type="button"
						onClick={handleToggle}
						className="md:pointer-events-none flex-shrink-0"
						aria-label={isExpanded ? "Close menu" : "Open menu"}
						aria-expanded={isExpanded}
					>
						<DofusIcon className="w-7 h-7 fill-white" />
					</button>

					<div className="hidden md:flex items-center gap-8">
						{NAV_LINKS.slice(2).map((link) => (
							<a
								key={link.href}
								href={link.href}
								className="text-[10px] text-zinc-400 hover:text-white transition-colors uppercase tracking-wider font-medium"
							>
								{link.label}
							</a>
						))}
						<button
							type="button"
							onClick={onLoginClick}
							aria-label="Sign in"
							className="text-[10px] text-black bg-white hover:bg-zinc-200 px-4 py-1.5 rounded-full font-bold transition-colors uppercase tracking-wider"
						>
							Login
						</button>
					</div>
				</div>

				<div
					className={cn(
						"md:hidden absolute top-full mt-3 left-1/2 -translate-x-1/2",
						"transition-all duration-300 ease-[cubic-bezier(0.4,0,0.2,1)]",
						isExpanded
							? "opacity-100 scale-100 translate-y-0 pointer-events-auto"
							: "opacity-0 scale-95 -translate-y-4 pointer-events-none",
					)}
				>
					<div className="bg-black/90 backdrop-blur-xl rounded-2xl border border-zinc-800 shadow-xl overflow-hidden">
						<div className="flex flex-col py-2 min-w-[180px]">
							{NAV_LINKS.map((link) => (
								<a
									key={link.href}
									href={link.href}
									onClick={handleClose}
									className="px-5 py-3 text-[10px] text-zinc-400 hover:text-white hover:bg-zinc-900/50 transition-all uppercase tracking-wider font-medium"
								>
									{link.label}
								</a>
							))}
							<div className="h-px bg-zinc-800 my-2 mx-3" />
							<div className="px-3 py-2">
								<button
									type="button"
									onClick={handleSignIn}
									className="w-full text-[10px] text-black bg-white hover:bg-zinc-200 px-4 py-2 rounded-full font-bold transition-all uppercase tracking-wider"
								>
									Login
								</button>
							</div>
						</div>
					</div>
				</div>
			</nav>

			{isExpanded && (
				<div
					className="md:hidden fixed inset-0 bg-black/30 z-40 animate-in fade-in duration-300"
					onClick={handleClose}
					aria-hidden="true"
				/>
			)}
		</>
	);
};

export default LandingNavbar;

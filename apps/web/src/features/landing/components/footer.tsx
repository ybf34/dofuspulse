import { Heart } from "lucide-react";
import DofusIcon from "@/components/icons/dofus-icon.tsx";

const Footer = () => {
	return (
		<footer className="w-full bg-black pt-20 pb-10 border-t border-white/5 relative z-20">
			<div className="max-w-6xl mx-auto px-4 flex flex-col items-center text-center">
				<div className="mb-12">
					<div className="p-4 bg-zinc-900/20 rounded-full border border-zinc-800/50 hover:border-zinc-700/50 transition-all duration-500 hover:scale-105">
						<DofusIcon className="w-10 h-10 text-white fill-white" />
					</div>
				</div>

				<div className="flex items-center justify-center gap-2 mb-10 text-sm">
					<span className="text-zinc-300">Made with</span>
					<Heart className="w-3 h-3 text-red-500 fill-red-500 animate-pulse" />
					<span className="text-zinc-300">for the Dofus Touch community</span>
				</div>

				<div className="space-y-3 text-xs leading-relaxed text-zinc-700 max-w-xl mx-auto border-t border-zinc-700/50 pt-8">
					<p>
						Dofus Pulse is an independent project, not affiliated with Ankama
						Games. DOFUS Touch and ANKAMA are trademarks of Ankama Games.
						Illustrations are the property of Ankama Studio and Dofus, all
						rights reserved.
					</p>

					<p>
						All data is community-aggregated from manual user submissions and
						updated periodically.
					</p>
				</div>

				<div className="mt-8 text-[0.65rem] text-zinc-700">
					© 2025 Dofus Pulse
				</div>
			</div>
		</footer>
	);
};

export default Footer;

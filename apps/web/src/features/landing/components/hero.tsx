import { motion } from "framer-motion";
import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";

interface HeroProps {
	onGetStartedClick: () => void;
	onIntroComplete?: () => void;
}

export default function Hero({
	onGetStartedClick,
	onIntroComplete,
}: HeroProps) {
	const [showContent, setShowContent] = useState(false);

	useEffect(() => {
		const timer = setTimeout(() => {
			setShowContent(true);
			onIntroComplete?.();
		}, 2500);

		return () => clearTimeout(timer);
	}, [onIntroComplete]);

	return (
		<section className="flex flex-col w-full min-h-screen bg-black">
			<div className="h-28" />

			<div className="flex-1 flex flex-col items-center justify-start pt-12 px-6 text-center max-w-4xl mx-auto">
				<motion.div
					initial={false}
					animate={{
						opacity: showContent ? 1 : 0,
						y: showContent ? 0 : 24,
					}}
					transition={{ duration: 0.8, ease: "easeOut" }}
				>
					<div className="flex flex-wrap justify-center items-center gap-3 mb-6">
						<span className="px-4 py-1.5 rounded-full border bg-white/5 text-[9px] font-mono text-zinc-300 uppercase tracking-widest">
							For Dofus Touch Players
						</span>
						<span className="px-4 py-1.5 rounded-full border border-amber-500/20 bg-amber-500/5 text-[9px] font-mono font-bold text-amber-400 uppercase tracking-widest flex items-center gap-2">
							<span className="w-1.5 h-1.5 rounded-full bg-amber-500" />
							Beta Access
						</span>
					</div>

					<h1 className="text-3xl md:text-4xl lg:text-5xl font-medium text-white tracking-tight leading-[1.1]">
						Advanced Market Intelligence.
						<br />
						<span className="md:whitespace-nowrap">
							Maximize Your Daily <span className="text-amber-400">Kamas.</span>
						</span>
					</h1>

					<Button
						variant="default"
						size="lg"
						className="rounded-full font-medium bg-white text-zinc-800 hover:bg-zinc-200 mt-5 shadow-[0_0_15px_rgba(255,255,255,0.1)] transition-all"
						onClick={onGetStartedClick}
					>
						Get Started
					</Button>
				</motion.div>

				<div className="mt-12 pb-20">
					<video
						className="w-[300px] md:w-[400px] max-h-[600px] object-contain pointer-events-none mx-auto"
						autoPlay
						loop
						muted
						playsInline
						src="/landing/hero-video.webm"
					/>
				</div>
			</div>
		</section>
	);
}

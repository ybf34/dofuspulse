import { motion } from "framer-motion";
import { useEffect, useState } from "react";
import { toast } from "sonner";
import { AuthModal } from "@/features/auth/components/auth-modal";
import Footer from "@/features/landing/components/footer";
import Hero from "@/features/landing/components/hero";
import LandingNavBar from "@/features/landing/components/landing-nav-bar";

export const LandingPage = () => {
	const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
	const [showNavbar, setShowNavbar] = useState(false);

	useEffect(() => {
		const params = new URLSearchParams(window.location.search);
		const error = params.get("error");

		if (error) {
			toast.error("Authentication was cancelled or failed");
			window.history.replaceState({}, document.title, window.location.pathname);
		}
	}, []);

	return (
		<div className="min-h-screen bg-black text-white overflow-hidden">
			{showNavbar && (
				<motion.div
					initial={{ opacity: 0, y: -16 }}
					animate={{ opacity: 1, y: 0 }}
					transition={{ duration: 0.6, ease: "easeOut" }}
					className="fixed top-8 left-1/2 transform -translate-x-1/2 z-[100]"
				>
					<LandingNavBar onLoginClick={() => setIsAuthModalOpen(true)} />
				</motion.div>
			)}

			<main>
				<Hero
					onGetStartedClick={() => setIsAuthModalOpen(true)}
					onIntroComplete={() => setShowNavbar(true)}
				/>
			</main>

			<Footer />

			<AuthModal open={isAuthModalOpen} onOpenChange={setIsAuthModalOpen} />
		</div>
	);
};

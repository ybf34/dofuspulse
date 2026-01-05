import { zodResolver } from "@hookform/resolvers/zod";
import { Loader2Icon } from "lucide-react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import {
	Form,
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { cn } from "@/lib/utils";

const SignupFormSchema = z
	.object({
		email: z.string().email("Please enter a valid email address"),
		password: z.string().min(8, "Password must be at least 8 characters"),
		confirmPassword: z
			.string()
			.min(8, "Password must be at least 8 characters"),
	})
	.refine((data) => data.password === data.confirmPassword, {
		message: "Passwords don't match",
		path: ["confirmPassword"],
	});

type SignupFormData = z.infer<typeof SignupFormSchema>;

interface RegisterFormProps {
	onSubmit: (data: { email: string; password: string }) => Promise<void>;
	isPending: boolean;
}

export function RegisterForm({ onSubmit, isPending }: RegisterFormProps) {
	const form = useForm<SignupFormData>({
		resolver: zodResolver(SignupFormSchema),
		defaultValues: {
			email: "",
			password: "",
			confirmPassword: "",
		},
	});

	const handleSubmit = async (data: SignupFormData) => {
		await onSubmit({
			email: data.email,
			password: data.password,
		});
	};

	return (
		<Form {...form}>
			<form
				onSubmit={form.handleSubmit(handleSubmit)}
				className={cn("flex flex-col gap-6")}
			>
				<div className="grid gap-6">
					<FormField
						control={form.control}
						name="email"
						render={({ field }) => (
							<FormItem className="grid gap-3">
								<FormLabel className="text-zinc-300">Email</FormLabel>
								<FormControl>
									<Input
										type="email"
										placeholder="m@example.com"
										className="bg-zinc-900 border-zinc-800 text-white placeholder:text-zinc-500"
										{...field}
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>
					<FormField
						control={form.control}
						name="password"
						render={({ field }) => (
							<FormItem className="grid gap-3">
								<FormLabel className="text-zinc-300">Password</FormLabel>
								<FormControl>
									<Input
										type="password"
										placeholder="At least 8 characters"
										className="bg-zinc-900 border-zinc-800 text-white placeholder:text-zinc-500"
										{...field}
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>
					<FormField
						control={form.control}
						name="confirmPassword"
						render={({ field }) => (
							<FormItem className="grid gap-3">
								<FormLabel className="text-zinc-300">
									Confirm Password
								</FormLabel>
								<FormControl>
									<Input
										type="password"
										placeholder="Confirm your password"
										className="bg-zinc-900 border-zinc-800 text-white placeholder:text-zinc-500"
										{...field}
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>
					<Button
						type="submit"
						className="w-full rounded-full font-hero bg-white text-black hover:bg-zinc-200"
						size="sm"
						disabled={isPending}
					>
						{isPending ? (
							<>
								<Loader2Icon className="mr-2 h-4 w-4 animate-spin" />
								Creating account...
							</>
						) : (
							"Sign up"
						)}
					</Button>
				</div>
			</form>
		</Form>
	);
}

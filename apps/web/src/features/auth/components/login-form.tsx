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

const LoginFormSchema = z.object({
	email: z.string().email("Please enter a valid email address"),
	password: z.string().min(8, "Password must be at least 8 characters"),
});

type LoginFormData = z.infer<typeof LoginFormSchema>;

interface LoginFormProps {
	onSubmit: (data: LoginFormData) => Promise<void>;
	isPending: boolean;
}

export function LoginForm({ onSubmit, isPending }: LoginFormProps) {
	const form = useForm<LoginFormData>({
		resolver: zodResolver(LoginFormSchema),
		defaultValues: {
			email: "",
			password: "",
		},
	});

	return (
		<Form {...form}>
			<form
				onSubmit={form.handleSubmit(onSubmit)}
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
								<div className="flex items-center">
									<FormLabel className="text-zinc-300">Password</FormLabel>
									<a
										href="/forgot-password"
										className="ml-auto text-sm text-zinc-400 hover:text-white underline-offset-4 hover:underline"
									>
										Forgot password?
									</a>
								</div>
								<FormControl>
									<Input
										type="password"
										className="bg-zinc-900 border-zinc-800 text-white"
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
								Please wait
							</>
						) : (
							"Login"
						)}
					</Button>
				</div>
			</form>
		</Form>
	);
}

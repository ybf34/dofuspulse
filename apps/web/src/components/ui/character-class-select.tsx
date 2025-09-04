import { Check, Mars, Venus } from "lucide-react";
import CharacterClassAvatar from "@/components/ui/character-class-avatar";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import {
	Tooltip,
	TooltipContent,
	TooltipProvider,
	TooltipTrigger,
} from "@/components/ui/tooltip";
import { characterCardDirectory, characterClassNames } from "@/utils/constants";

type Props = {
	classValue: string;
	genderValue: "m" | "f";
	onClassChange: (cls: string) => void;
	onGenderChange: (g: "m" | "f") => void;
};

export default function CharacterClassSelect({
	classValue,
	genderValue,
	onClassChange,
	onGenderChange,
}: Props) {
	return (
		<TooltipProvider>
			<div className="space-y-2">
				<div className="flex justify-end">
					<RadioGroup
						value={genderValue}
						onValueChange={(g) => g && onGenderChange(g as "m" | "f")}
						className="flex gap-0 rounded-lg border border-gray-700"
						aria-label="Select character gender"
					>
						<Label
							htmlFor="gender-m"
							className="flex h-8 w-8 cursor-pointer items-center justify-center rounded-s-lg has-[[data-state=checked]]:bg-blue-500/10"
						>
							<RadioGroupItem value="m" className="sr-only" />
							<Mars className="h-4 w-4 text-blue-500" />
						</Label>
						<Label
							htmlFor="gender-f"
							className="flex h-8 w-8 cursor-pointer items-center justify-center rounded-e-lg has-[[data-state=checked]]:bg-pink-500/10"
						>
							<RadioGroupItem value="f" className="sr-only" />
							<Venus className="h-4 w-4 text-pink-500" />
						</Label>
					</RadioGroup>
				</div>

				<RadioGroup
					value={classValue}
					onValueChange={onClassChange}
					className="flex pt-2"
				>
					<div className="mx-auto grid [grid-template-columns:repeat(5,max-content)] gap-1.5">
						{characterClassNames.map((name) => {
							const selected = classValue === name;
							const imgId = `${name}-${genderValue}`;

							return (
								<Tooltip key={imgId} delayDuration={3000}>
									<TooltipTrigger asChild>
										<Label
											htmlFor={imgId}
											className="relative flex cursor-pointer justify-center rounded-md transition-transform duration-200 hover:scale-105 focus:outline-none"
										>
											<RadioGroupItem
												value={name}
												id={imgId}
												className="sr-only"
											/>
											<CharacterClassAvatar
												imageSrc={`${characterCardDirectory}${imgId}.png`}
												altText={name}
												isSelected={selected}
												size={49}
											/>
											{selected && (
												<div className="absolute right-1 top-1 flex items-center justify-center rounded-full bg-black/60 p-0.5 shadow-sm">
													<Check
														className="h-3 w-3 text-white"
														strokeWidth={2.5}
													/>
												</div>
											)}
										</Label>
									</TooltipTrigger>
									<TooltipContent className="capitalize">{name}</TooltipContent>
								</Tooltip>
							);
						})}
					</div>
				</RadioGroup>
			</div>
		</TooltipProvider>
	);
}

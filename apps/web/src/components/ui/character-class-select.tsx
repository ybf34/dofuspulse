import {Check, Mars, Venus} from "lucide-react";
import CharacterClassAvatar from "@/components/ui/character-class-avatar";
import {Label} from "@/components/ui/label";
import {RadioGroup, RadioGroupItem} from "@/components/ui/radio-group";
import {Tooltip, TooltipContent, TooltipProvider, TooltipTrigger,} from "@/components/ui/tooltip";
import {type CharacterClassName, characterClassNames,} from "@/services/api/api.types";
import {characterCardDirectory} from "@/utils/constants";

export type CharacterSelection = {
  characterClass: CharacterClassName;
  gender: "m" | "f";
};

type CharacterClassSelectProps = {
  value: CharacterSelection;
  onChange: (val: CharacterSelection) => void;
  className?: string;
};

export default function CharacterClassSelect({
                                               value,
                                               onChange,
                                               className,
                                             }: CharacterClassSelectProps) {
	return (
		<TooltipProvider>
      <div className={`space-y-4 ${className}`}>
				<div className="flex justify-end">
					<RadioGroup
              value={value.gender}
              onValueChange={(g) =>
                  onChange({...value, gender: g as "m" | "f"})
              }
						className="flex gap-0 rounded-lg border border-gray-700"
						aria-label="Select character gender"
					>
						<Label
							htmlFor="gender-m"
							className="flex h-8 w-8 cursor-pointer items-center justify-center rounded-s-lg has-[[data-state=checked]]:bg-blue-500/10"
						>
              {/** biome-ignore lint/correctness/useUniqueElementIds: radio */}
              <RadioGroupItem value="m" id="gender-m" className="sr-only"/>
							<Mars className="h-4 w-4 text-blue-500" />
						</Label>
						<Label
							htmlFor="gender-f"
							className="flex h-8 w-8 cursor-pointer items-center justify-center rounded-e-lg has-[[data-state=checked]]:bg-pink-500/10"
						>
              {/** biome-ignore lint/correctness/useUniqueElementIds: radio */}
              <RadioGroupItem value="f" id="gender-f" className="sr-only"/>
							<Venus className="h-4 w-4 text-pink-500" />
						</Label>
					</RadioGroup>
				</div>

				<RadioGroup
            value={value.characterClass}
            onValueChange={(cls) =>
                onChange({...value, characterClass: cls as CharacterClassName})
            }
					className="flex pt-2"
				>
					<div className="mx-auto grid [grid-template-columns:repeat(5,max-content)] gap-1.5">
            {Object.values(characterClassNames).map((name) => {
              const selected = value.characterClass === name;
              const imgId = `${name.toLowerCase()}-${value.gender}`;

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

import {ReactComponent as DofusSvgIcon} from "@/assets/dofus-pulse.svg";

export type DofusIconProps = {
  className?: string;
};

export default function DofusIcon({className}: DofusIconProps) {
  return <DofusSvgIcon className={className}/>;
}

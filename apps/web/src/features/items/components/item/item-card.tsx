import type {ItemDetailsDto} from "@/api/model";
import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {ItemEffectsList} from "@/features/items/components/item/item-effects-list.tsx";

export type ItemCardProps = {
  itemDetails: ItemDetailsDto;
};
export default function ItemCard({itemDetails}: ItemCardProps) {
  return (
      <Card className="w-full max-w-sm">
        <CardHeader>
          <CardTitle>{itemDetails.name}</CardTitle>
          <CardDescription>Card Description</CardDescription>
          <CardAction>Card Action</CardAction>
        </CardHeader>
        <CardContent>
          <p>Level: {itemDetails.level} </p>
          <p>Type: {itemDetails.itemTypeId} </p>
          <p>IconId: {itemDetails.iconId}</p>
          <ItemEffectsList stats={itemDetails.possibleEffects}/>
        </CardContent>
        <CardFooter>
          <p>Card Footer</p>
        </CardFooter>
      </Card>
  );
}

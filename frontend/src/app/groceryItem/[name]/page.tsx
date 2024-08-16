import { cn } from "@/lib/utils";
import { GroceryItem } from "@/components/grocery-item/grocery-item";

export default function Grocery() {
  return (
    <div className="flex justify-center w-full py-12">
      <GroceryItem />
    </div>
  );
}

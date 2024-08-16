import { cn } from "@/lib/utils";
import { logo_font } from "./fonts";

export default function Logo() {
  return (
    <div
      className={cn(
        `${logo_font.className} antialiased flex items-center text-2xl`,
      )}
    >
      Room<span className={cn("text-destructive")}>M8</span>
    </div>
  );
}

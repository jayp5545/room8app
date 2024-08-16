import { Mulish, Righteous } from "next/font/google";

export const logo_font = Righteous({ subsets: ["latin"], weight: ["400"] });
export const primary_font = Mulish({
  subsets: ["latin"],
  weight: ["200", "500", "800", "1000"],
});

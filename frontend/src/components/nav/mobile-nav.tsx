"use client";

import { useState } from "react";
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet";
import { Button } from "@/components/ui/button";
import { Menu as MenuIcon } from "lucide-react";
import { cn } from "@/lib/utils";
import { links } from "@/constants/links";
import Link from "next/link";

export default function MobileNav() {
  const [open, setOpen] = useState(false);

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <Button variant="outline" size="icon" className="lg:hidden">
          <MenuIcon />
        </Button>
      </SheetTrigger>

      <SheetContent side="right">
        <SheetHeader className="my-4">
          <SheetTitle className="text-left">go to</SheetTitle>
        </SheetHeader>
        <div className={cn("flex flex-col items-start my-4")}>
          {links.map((link, index) => (
            <Link key={index} href={link.link}>
              <Button
                variant="link"
                onClick={() => {
                  setOpen(false);
                }}
              >
                {link.title}
              </Button>
            </Link>
          ))}
        </div>
      </SheetContent>
    </Sheet>
  );
}

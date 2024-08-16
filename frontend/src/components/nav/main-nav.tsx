"use client";

import { cn } from "@/lib/utils";
import Logo from "../logo";
import { ThemeToggler } from "../theme-toggler";
import MobileNav from "./mobile-nav";
import { links } from "@/constants/links";
import Link from "next/link";
import { Button } from "../ui/button";
import { useAuth } from "@/context/AuthContext";
import { usePathname, useRouter } from "next/navigation";
import { ProfileDialogue } from "./profile-dialogue";

export default function NavBar() {
  const { user, logout } = useAuth();
  const router = useRouter();
  const currentRoute = usePathname();

  return (
    <nav
      className={cn(
        "flex flex-row items-center mt-4 px-2 lg:mt-6 lg:px-8 w-full justify-between",
      )}
    >
      <Link key="Homepage" href="/">
        <Logo />
      </Link>
      <div className={cn("flex gap-4")}>
        <div className={cn("hidden lg:flex items-center mx-4")}>
          {links.map((link, index) => {
            if (currentRoute == link.link) {
              return (
                <Button
                  key={index}
                  variant={"secondary"}
                  className={cn(
                    "place-items-center mx-1 p-1 text-md font-medium",
                    currentRoute == link.link ? "" : "",
                  )}
                >
                  <Link href={link.link} className={cn("mx-2")}>
                    {link.title}
                  </Link>
                </Button>
              );
            }
            return (
              <Button
                key={index}
                variant={"outline"}
                className={cn(
                  "place-items-center mx-1 p-1 text-md font-medium",
                  currentRoute == link.link ? "" : "",
                )}
              >
                <Link href={link.link} className={cn("mx-2")}>
                  {link.title}
                </Link>
              </Button>
            );
          })}
        </div>
        {user ? (
          <Button onClick={logout}>Logout</Button>
        ) : (
          <Button onClick={() => router.push("/login")}>Login</Button>
        )}
        <ThemeToggler />
        <MobileNav />
      </div>
    </nav>
  );
}

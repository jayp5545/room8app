"use client";

import { usePathname } from "next/navigation";
import { PathBreadCrumb } from "./path-breadcrumb";
import { useAuth } from "@/context/AuthContext";

export function PageHeader() {
  let currentRoute = usePathname();
  const { room } = useAuth();
  currentRoute = currentRoute.replaceAll("/", "");
  currentRoute = currentRoute.charAt(0).toUpperCase() + currentRoute.slice(1);

  if (!room) {
    return <></>;
  }

  return (
    <div className="flex items-center h-16 m-2 my-4 p-2 px-6">
      <PathBreadCrumb />
    </div>
  );
}

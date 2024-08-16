"use client";

import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { useAuth } from "@/context/AuthContext";
import { usePathname } from "next/navigation";

export function PathBreadCrumb() {
  let currentRoute = usePathname();
  const { room } = useAuth();
  currentRoute = currentRoute.replaceAll("/", "");
  currentRoute = currentRoute.charAt(0).toUpperCase() + currentRoute.slice(1);

  if (!room) {
    return <></>;
  }

  return (
    <Breadcrumb>
      <BreadcrumbList className="text-lg">
        <BreadcrumbItem>
          <BreadcrumbLink href="/">Home</BreadcrumbLink>
        </BreadcrumbItem>
        <BreadcrumbSeparator />
        <BreadcrumbItem>
          <BreadcrumbPage>{room?.name}</BreadcrumbPage>
        </BreadcrumbItem>
        <BreadcrumbSeparator />
        <BreadcrumbItem>
          <BreadcrumbPage className="text-2xl font-semibold">
            {currentRoute}
          </BreadcrumbPage>
        </BreadcrumbItem>
      </BreadcrumbList>
    </Breadcrumb>
  );
}

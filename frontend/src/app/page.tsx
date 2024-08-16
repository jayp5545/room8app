"use client";

import ProtectedRoute from "@/components/protected-route";
import { cn } from "@/lib/utils";
import { useRouter } from "next/navigation";

export default function Home() {
  const router = useRouter();
  router.push("/announcements");

  return (
    <ProtectedRoute>
      <main></main>
    </ProtectedRoute>
  );
}

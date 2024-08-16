"use client";

import { cn } from "@/lib/utils";
import { GroceryList } from "@/components/grocery-list/grocery-list";
import ProtectedRoute from "@/components/protected-route";
import { PageHeader } from "@/components/page-header";
import EmptyRoomCard from "@/components/empty-room";
import { useAuth } from "@/context/AuthContext";

export default function Grocery() {
  const { room } = useAuth();

  if (!room) {
    return (
      <div className="flex min-h-[600px] justify-center items-center">
        <EmptyRoomCard />
      </div>
    );
  }

  return (
    <ProtectedRoute>
      <PageHeader />
      <div className="flex justify-center w-full py-12">
        <div className="w-full">
          <GroceryList />
        </div>
      </div>
    </ProtectedRoute>
  );
}

"use client";

import React from "react";
import ProtectedRoute from "@/components/protected-route";
import { PageHeader } from "@/components/page-header";
import ExpenseComponent from "@/components/expenses/expenseComponent";
import EmptyRoomCard from "@/components/empty-room";
import { useAuth } from "@/context/AuthContext";

const ExpensesPage = () => {
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
      <ExpenseComponent />
    </ProtectedRoute>
  );
};

export default ExpensesPage;

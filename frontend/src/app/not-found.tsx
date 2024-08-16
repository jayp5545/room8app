import React from "react";
import ProtectedRoute from "@/components/protected-route";
import { Separator } from "@/components/ui/separator";

const NotFoundPage = () => {
  return (
    <div className="flex w-full h-screen items-center justify-center gap-4">
      <span className="text-2xl font-bold">404</span>
      <Separator orientation="vertical" className="h-12" />
      <span className="text-lg">This page could not be found</span>
    </div>
  );
};

export default NotFoundPage;

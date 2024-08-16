"use client";

import { useAuth } from "@/context/AuthContext";
import { useRouter } from "next/navigation";
import { ReactNode, useEffect } from "react";
import { Loading } from "./loading/loading";

type ProtectRouteProps = {
  children: ReactNode;
};

const ProtectedRoute = ({ children }: ProtectRouteProps) => {
  const { user, token, isLoading } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!isLoading && !user && !token) {
      router.push("/login");
    }
  }, [user, token, isLoading, router]);

  if (isLoading) {
    return <Loading />;
  }

  if (!user || !token) {
    return null;
  }

  return children;
};

export default ProtectedRoute;

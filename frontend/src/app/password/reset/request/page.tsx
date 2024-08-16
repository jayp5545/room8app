import ResetPasswordFormCard from "@/components/forms/reset-password/reset-password-form-card";
import { cn } from "@/lib/utils";
import { Suspense } from "react";

export default function ResetPasswordRequest() {
  return (
    <div
      className={cn("flex items-center justify-center h-[calc(100vh-10rem)]")}
    >
      <Suspense>
        <ResetPasswordFormCard />
      </Suspense>
    </div>
  );
}

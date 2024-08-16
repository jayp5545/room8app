import PasswordResetErrorCard from "@/components/reset-password/reset-password-error-card";
import { cn } from "@/lib/utils";

export default function ResetPasswordError() {
  return (
    <div
      className={cn("flex items-center justify-center h-[calc(100vh-10rem)]")}
    >
      <PasswordResetErrorCard />
    </div>
  );
}

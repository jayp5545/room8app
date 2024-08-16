import PasswordResetDoneCard from "@/components/reset-password/reset-password-done-card";
import { cn } from "@/lib/utils";

export default function ResetPasswordDone() {
  return (
    <div
      className={cn("flex items-center justify-center h-[calc(100vh-10rem)]")}
    >
      <PasswordResetDoneCard />
    </div>
  );
}

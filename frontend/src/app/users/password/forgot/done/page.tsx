import ForgotPasswordDoneCard from "@/components/forgot-password/forgot-password-done-card";
import { cn } from "@/lib/utils";

export default function ForgotPasswordDone() {
  return (
    <div
      className={cn("flex items-center justify-center h-[calc(100vh-10rem)]")}
    >
      <ForgotPasswordDoneCard />
    </div>
  );
}

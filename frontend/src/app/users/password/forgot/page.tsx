import ForgotPasswordForm from "@/components/forms/forgot-password/forgot-password-form-card";
import { cn } from "@/lib/utils";

export default function ForgotPassword() {
  return (
    <div
      className={cn("flex items-center justify-center h-[calc(100vh-10rem)]")}
    >
      <ForgotPasswordForm />
    </div>
  );
}

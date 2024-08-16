import RegisterFormCard from "@/components/forms/register/register-form-card";
import { cn } from "@/lib/utils";

export default function RegisterPage() {
  return (
    <div
      className={cn("flex items-center justify-center ")}
    >
      <RegisterFormCard />
    </div>
  );
}

import LoginFormCard from "@/components/forms/login/login-form-card";
import { cn } from "@/lib/utils";

export default function Login() {
  return (
    <div
      className={cn("flex items-center justify-center h-[calc(100vh-10rem)]")}
    >
      <LoginFormCard />
    </div>
  );
}

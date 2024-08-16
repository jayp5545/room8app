"use client";

import {
  Card,
  CardDescription,
  CardFooter,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";

export default function PasswordResetDoneCard() {
  const router = useRouter();

  return (
    <Card className="w-[18rem] lg:w-[40rem] py-6 px-2">
      <CardTitle className="m-4">Password Reset Successful!</CardTitle>
      <CardDescription className="m-4">
        Your password has been successfully reset. You can now log in to your
        account with your new password.
      </CardDescription>
      <CardFooter>
        <Button type="submit" className="w-full" onClick={() => router.replace("/login")}>
          Back to login
        </Button>
      </CardFooter>
    </Card>
  );
}

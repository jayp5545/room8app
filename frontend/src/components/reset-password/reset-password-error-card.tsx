"use client";

import {
  Card,
  CardDescription,
  CardFooter,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";

export default function PasswordResetErrorCard() {
  const router = useRouter();

  return (
    <Card className="w-[18rem] lg:w-[40rem] py-6 px-2">
      <CardTitle className="m-4">Error in the token</CardTitle>
      <CardDescription className="m-4">
        The password reset token is invalid or expired. Please try generating a
        new password reset token.
      </CardDescription>
      <CardFooter>
        <Button type="submit" className="w-full" onClick={() => router.replace("/users/password/forgot")}>
          Back to Forgot Password
        </Button>
      </CardFooter>
    </Card>
  );
}

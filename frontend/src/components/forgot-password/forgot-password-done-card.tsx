"use client";

import { Card, CardDescription, CardFooter, CardTitle } from "@/components/ui/card";
import { Button } from "../ui/button";
import { useRouter } from "next/navigation";

export default function ForgotPasswordDoneCard() {
  const router = useRouter();

  return (
    <Card className="w-[18rem] lg:w-[40rem] py-6 px-2">
      <CardTitle className="m-4">Assistance is on its way!</CardTitle>
      <CardDescription className="m-4">
        If your user account is found, you will receive an email containing
        detailed instructions on how to reset your password.
      </CardDescription>

      <CardFooter>
        <Button type="submit" className="w-full" onClick={() => router.replace("/login")}>
          Back to login
        </Button>
      </CardFooter>
    </Card>
  );
}

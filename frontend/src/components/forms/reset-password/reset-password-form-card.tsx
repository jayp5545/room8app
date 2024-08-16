"use client";

import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { PasswordInput } from "@/components/ui/password-input";

// form imports
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { useRouter, useSearchParams } from "next/navigation";
import axios from "axios";
import config from "@/config";

// zod schema for forgot password form
const ResetPasswordSchema = z
  .object({
    password: z
      .string()
      .min(8, { message: "Password must be atleast 8 characters" }),
    confirmpassword: z
      .string()
      .min(8, { message: "Password must be atleast 8 characters" }),
  })
  .refine(
    ({ password, confirmpassword }) => {
      return password === confirmpassword;
    },
    {
      message: "Passwords must match!",
      path: ["confirmpassword"],
    }
  );

export default function ResetPasswordFormCard() {
  const router = useRouter();

  const searchParams = useSearchParams();
  let currentToken = searchParams.get("token");
  currentToken = currentToken ?? "undefined";

  // Shows submitted details
  async function onSubmit(data: z.infer<typeof ResetPasswordSchema>) {
    const BACKEND_RESET_PASSWORD_API =
      `${config.apiBaseUrl}/users/password/reset`;
    const requestBody = {
      token: currentToken,
      password: data.password,
      confirmPassword: data.confirmpassword,
    };

    await axios
      .post(BACKEND_RESET_PASSWORD_API, requestBody , { validateStatus: function (status) {
        return status === 200 || status === 400
      }})
      .then((response) => {
        const status_code = response.status;

        if (status_code === 200) {
          router.push("/users/password/reset/done");
        } else if (status_code === 400) {
          router.push("/users/password/reset/error");
        }
      })
      .catch((error) => {
        console.error(`Internal Server Error!!!, error: ${error}`);
        router.push("/404");
      });
  }

  // define the form object
  const form = useForm<z.infer<typeof ResetPasswordSchema>>({
    resolver: zodResolver(ResetPasswordSchema),
    defaultValues: {
      password: "",
      confirmpassword: "",
    },
  });

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)}>
        <Card className="w-[18rem] lg:w-[30rem] py-6 px-2">
          <CardTitle className="m-4">Reset Password</CardTitle>
          <CardDescription className="m-4">
            Please enter the new password and we will change the password for you.
          </CardDescription>
          <CardContent className="my-8">
            <div className="grid w-full items-center gap-4">
              <div className="flex flex-col space-y-1.5">
                <FormField
                  control={form.control}
                  name="password"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Password</FormLabel>
                      <FormControl>
                        <PasswordInput
                          {...field}
                          placeholder="password"
                          autoComplete="new-password"
                          required
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
              <div className="flex flex-col space-y-1.5">
                <FormField
                  control={form.control}
                  name="confirmpassword"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Confirm Password</FormLabel>
                      <FormControl>
                        <PasswordInput
                          {...field}
                          placeholder="confirm password"
                          autoComplete="new-password"
                          required
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
            </div>
          </CardContent>
          <CardFooter>
            <Button type="submit" className="w-full">
              Submit
            </Button>
          </CardFooter>
        </Card>
      </form>
    </Form>
  );
}

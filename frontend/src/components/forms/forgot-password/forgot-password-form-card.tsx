"use client";

import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { toast } from "@/components/ui/use-toast";

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
import { useRouter } from "next/navigation";
import axios from "axios";
import { Input } from "@/components/ui/input";
import config from "@/config";

// zod schema for forgot password form
const ForgotPasswordSchema = z.object({
  email: z.string().email(),
});

export default function ForgotPasswordForm() {
  const router = useRouter();

  // Shows submitted details
  async function onSubmit(data: z.infer<typeof ForgotPasswordSchema>) {
    const BACKEND_FORGOT_PASSWORD_API =
      `${config.apiBaseUrl}/users/password/forgot`;
    const requestBody = {
      email: data.email,
    };

    await axios
      .post(BACKEND_FORGOT_PASSWORD_API, requestBody, {
        validateStatus: function (status) {
          return status === 200;
        },
      })
      .then(() => {
        router.replace("/users/password/forgot/done");
      })
      .catch((error) => {
        console.error(`Internal Server Error!!!, error: ${error}`);
        toast({
          title: "something went wrong!",
        });
      });
  }

  // define the form object
  const form = useForm<z.infer<typeof ForgotPasswordSchema>>({
    resolver: zodResolver(ForgotPasswordSchema),
    defaultValues: {
      email: "",
    },
  });

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)}>
        <Card className="w-[18rem] lg:w-[30rem] py-6 px-2">
          <CardTitle className="m-4">Forgot Password?</CardTitle>
          <CardDescription className="m-4">
            Please enter the email address associated with your account, and we
            will send you instructions on how to reset your password.
          </CardDescription>
          <CardContent className="my-8">
            <div className="grid w-full items-center gap-4">
              <div className="flex flex-col space-y-1.5">
                <FormField
                  control={form.control}
                  name="email"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Email</FormLabel>
                      <FormControl>
                        <Input
                          {...field}
                          placeholder="email"
                          autoComplete="email"
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

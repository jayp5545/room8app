"use client";

import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { PasswordInput } from "@/components/ui/password-input";
import { toast } from "@/components/ui/use-toast";
import { useEffect } from "react";
import config from "@/config";

// form imports
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import axios from "axios";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";

// zod schema for login form
const LoginSchema = z.object({
  email: z.string().email(),
  password: z
    .string()
    .min(8, { message: "Password must be atleast 8 characters" }),
});

export default function LoginFormCard() {
  const { login } = useAuth();
  const router = useRouter();

  const BACKEND_LOGIN_API =
  config.apiBaseUrl + "/api/v1/auth/login";
  async function onSubmit(data: z.infer<typeof LoginSchema>) {
 
    const requestBody = {
      email: data.email,
      password: data.password,
    };

    const res = await axios
      .post(BACKEND_LOGIN_API, requestBody, {
        validateStatus: function (status) {
          return status === 200 || status === 400;
        },
      })
      .then((response) => {
        const status_code = response.status;
        console.log(response);
        if (status_code === 200) {
          const { email, token, roomDTOResponse } = response.data;
          login(email, token, roomDTOResponse);
          router.replace("/");
        }
        if (status_code === 400) {
          toast({
            title: "Invalid Login",
            description: <p>{"Email or Password is incorrect"}</p>,
          });
        }
      })
      .catch((error) => {
        console.error(`Internal Server Error!!!, error: ${error}`);
        toast({
          title: "something went wrong!",
        });
      });
  }

  // define the form object
  const form = useForm<z.infer<typeof LoginSchema>>({
    resolver: zodResolver(LoginSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  useEffect(() => {
    console.log(BACKEND_LOGIN_API);
  },[]);

  return (
    <div className="mt-40">
     
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)}>
          <Card className="w-[18rem] lg:w-[30rem] py-6 px-2">
            <CardTitle className="m-4">Login</CardTitle>
            <CardDescription className="m-4">
              Welcome back! Log in to access your expenses, tasks, groceries, and
              more.
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
                            autoComplete="current-password"
                            required
                          />
                        </FormControl>
                        <FormMessage />
                        <FormDescription>
                          <span
                            className="text-accent-foreground hover:cursor-pointer hover:text-destructive"
                            onClick={() => router.push("/users/password/forgot")}
                          >
                            Forgot Password?
                          </span>
                        </FormDescription>
                      </FormItem>
                    )}
                  />
                </div>
              </div>
            </CardContent>
            <CardFooter>
              <div className="flex flex-col w-full text-sm">
                <Button type="submit" className="w-full">
                  Login
                </Button>
                <div className="flex justify-end m-2 gap-4">
                  Not a member?
                  <span
                    className="text-accent-foreground hover:cursor-pointer hover:text-destructive"
                    onClick={() => router.push("/register")}
                  >
                    Register
                  </span>
                </div>
              </div>
            </CardFooter>
          </Card>
        </form>
      </Form>
    </div>
  );
}

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
import config from "@/config";
import { useRouter } from "next/navigation";
import axios from "axios";

// zod schema for login form
const RegisterSchema = z
  .object({
    fname: z.string(),
    lname: z.string(),
    email: z.string().email(),
    password: z
      .string()
      .min(8, { message: "Password must be atleast 8 characters" }),
    confirmpassword: z.string(),
  })
  .refine(({ password, confirmpassword }) => password === confirmpassword, {
    message: "passwords do not match",
    path: ["confirmpassword"],
  });

export default function RegisterFormCard() {
  const router = useRouter();

  async function onSubmit(data: z.infer<typeof RegisterSchema>) {
    const BACKEND_REGISTER_API =
    config.apiBaseUrl + "/api/v1/auth/register";
    const requestBody = {
      firstName: data.fname,
      lastName: data.lname,
      email: data.email,
      password: data.password,
    };

    await axios
      .post(BACKEND_REGISTER_API, requestBody, {
        validateStatus: function (status) {
          return status === 200 || status === 400;
        },
      })
      .then((response) => {
        const status_code = response.status;
        if (status_code === 200) {
          toast({
            title: "Account created sucessfully",
          });
          router.replace("/");
        }
        if (status_code === 400) {
          toast({
            title: "Invalid Details",
            description: <p>{"Something is wrong"}</p>,
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
  const form = useForm<z.infer<typeof RegisterSchema>>({
    resolver: zodResolver(RegisterSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  return (
    <div className="mt-10">
   
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)}>
        <Card className="mt-10 w-[18rem] lg:w-[30rem] py-6 px-2">
          <CardTitle className="m-4">Register</CardTitle>
          <CardDescription className="m-4">
            Join our community to manage expenses, tasks, groceries, and more.
            Register now to get started!
          </CardDescription>
          <CardContent className="my-8">
            <div className="grid w-full items-center gap-4">
              <div className="flex flex-col space-y-1.5">
                <FormField
                  control={form.control}
                  name="fname"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>First Name</FormLabel>
                      <FormControl>
                        <Input
                          {...field}
                          placeholder="first name"
                          autoComplete="given-name"
                          required
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
              <div className="grid w-full items-center gap-4">
                <div className="flex flex-col space-y-1.5">
                  <FormField
                    control={form.control}
                    name="lname"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Last Name</FormLabel>
                        <FormControl>
                          <Input
                            {...field}
                            placeholder="last name"
                            autoComplete="family-name"
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
                      </FormItem>
                    )}
                  />
                </div>
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
                          autoComplete="current-password"
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
            <div className="flex flex-col w-full text-sm">
              <Button type="submit" className="w-full">
                Register
              </Button>
              <div className="flex justify-end m-2 gap-4">
                Already a member?
                <span
                  className="text-accent-foreground hover:cursor-pointer hover:text-destructive"
                  onClick={() => router.push("/login")}
                >
                  Login
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

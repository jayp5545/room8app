// @ts-nocheck
"use client";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, FieldValues } from "react-hook-form";
import { z } from "zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/context/AuthContext";
import { useRouter } from "next/navigation";
import axios from "axios";
import config from "@/config";
import { toast } from "../ui/use-toast";
import { Toaster } from "../ui/toaster";

// Define the form schema using Zod
const formSchema = z.object({
  name: z.string().min(1, "Task name is required"),
  description: z.string().optional(),
  taskDate: z.string().refine((val) => !isNaN(Date.parse(val)), {
    message: "Invalid date format",
  }),
  assignedTO: z.string().min(1, "Assigned name is required"),
});

type FormValues = z.infer<typeof formSchema>;

export default function AddTask() {
  const { token } = useAuth();
  const router = useRouter();
  const { user } = useAuth();

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
  });

  const headers = {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };

  // Handle form submission
  const onSubmit = async (values: FormValues) => {
    // await addTask(values);
    console.log({ values });
    try {
      const res = await axios.post(`${config.apiBaseUrl}/task/add`, values, {
        headers,
        validateStatus: (status) => {
          return status === 200 || status === 400;
        },
      });
      console.log({ res });

      if (res.status === 400) {
        console.log(res);
        toast({
          title: "Error",
          description: res.data?.errorMessage,
          status: "error",
        });
        return;
      }

      toast({
        title: "Success",
        description: "Task added successfully",
        status: "success",
      });
    } catch (error) {
      // toast({
      //   title: "Error",
      //   description: error.message,
      //   status: "error",
      // });
    }
    router.push("/tasks");
  };

  return (
    <div>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
          <FormField
            control={form.control}
            name="name"
            render={({ field }: { field: FieldValues }) => (
              <FormItem>
                <FormLabel>Task Name</FormLabel>
                <FormControl>
                  <Input placeholder="Enter task name" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="taskDate"
            render={({ field }: { field: FieldValues }) => (
              <FormItem>
                <FormLabel>Task Date</FormLabel>
                <FormControl>
                  <Input type="date" placeholder="Enter task date" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="assignedTO"
            render={({ field }: { field: FieldValues }) => (
              <FormItem>
                <FormLabel>Assigned To</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Enter name of person assigned"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="description"
            render={({ field }: { field: FieldValues }) => (
              <FormItem>
                <FormLabel>Task Description</FormLabel>
                <FormControl>
                  <Input placeholder="Enter task description" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button type="submit">Submit</Button>
        </form>
      </Form>
      <Toaster />
    </div>
  );
}

"use client";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, Controller, FieldValues } from "react-hook-form";
import { z } from "zod";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/context/AuthContext";
import { useRouter } from "next/navigation";
import { useParams } from "next/navigation";
import axios from "axios";
import { useEffect } from "react";
import { TaskType } from "@/types/types";
import config from "@/config";
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

export default function UpdateTask() {
  const { token } = useAuth();
  const router = useRouter();
  const { user } = useAuth();
  const params = useParams();

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
  });

  const headers = {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };

  useEffect(() => {
    if (params.id) {
      axios
        .get(
          `${config.apiBaseUrl}/task/get?id=${params.id}`,
          { headers },
        )
        .then((response) => {
          const { name, description, taskDate, assignedTo } = response.data;
          console.log(assignedTo);
          const assignedTO = `${assignedTo.firstName} ${assignedTo.lastName}`;
          form.reset({
            name,
            description,
            taskDate,
            assignedTO,
          });
        })
        .catch((error) => {
          console.error("Failed to fetch task data", error);
        });
    }
  }, [params.id]);

  // Handle form submission
  const onSubmit = async (values: FormValues) => {
    // await addTask(values);
    console.log({ values });
    try {
      const res = await axios.put(
        `${config.apiBaseUrl}/task/update`,
        values,
        { headers },
      );
      console.log({ res });
      router.push("/tasks");
    } catch (error) {
      //console.log(error.response.data)
    }
  };

  return (
    <div className="flex justify-center w-full py-12">
      <div className="w-[40%] ">
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <FormField
              control={form.control}
              name="name"
              render={({ field }: { field: FieldValues }) => (
                <FormItem>
                  <FormLabel>Task Name</FormLabel>
                  <FormControl>
                    <Input disabled placeholder="Enter task name" {...field} />
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
                    <Input
                      type="date"
                      placeholder="Enter task date"
                      {...field}
                    />
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
            <Button type="submit">Update</Button>
          </form>
        </Form>
      </div>
    </div>
  );
}

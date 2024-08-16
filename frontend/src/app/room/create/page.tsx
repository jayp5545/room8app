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
import { toast } from "@/components/ui/use-toast";
import { cn } from "@/lib/utils";
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
import axios from "axios";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import config from "@/config";

// zod schema for create room form
const createRoom = z.object({
  roomName: z
    .string()
    .min(5, { message: "Room name must be at least 5 characters long" })
    .regex(/[A-Z]/, {
      message: "Room name must contain at least one uppercase letter",
    })
    .regex(/[a-z]/, {
      message: "Room name must contain at least one lowercase letter",
    }),
});

export default function CreateRoomFormCard() {
  const router = useRouter();
  const { token, setRoom } = useAuth();

  async function onSubmit(data: z.infer<typeof createRoom>) {
    const BACKEND_API = config.apiBaseUrl;
    const BACKEND_CREATE_ROOM_API = BACKEND_API + "/api/v1/room/create";

    const requestBody = {
      name: data.roomName,
    };

    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };

    await axios
      .post(BACKEND_CREATE_ROOM_API, requestBody, {
        validateStatus: function (status) {
          return status === 200 || status === 400;
        },
        headers: headers,
      })
      .then((response) => {
        const status_code = response.status;
        if (status_code === 200) {
          toast({
            title: "Room has been created successfully",
          });
          router.replace("/");
          setRoom(response.data);
        }
        if (status_code === 400) {
          toast({
            title: "Error",
            description: <p>{response.data?.errorMessage}</p>,
          });
        }
      })
      .catch((error) => {
        console.error(`Internal Server Error!!!, error: ${error}`);
        toast({
          title: "Couldn't join the room",
          description: "Please try again...",
        });
      });
  }

  // define the form object
  const form = useForm<z.infer<typeof createRoom>>({
    resolver: zodResolver(createRoom),
    defaultValues: {
      roomName: "",
    },
  });

  return (
    <div
      className={cn("flex items-center justify-center h-[calc(100vh-10rem)]")}
    >
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)}>
          <Card className="w-[18rem] lg:w-[30rem] py-6 px-2">
            <CardTitle className="m-4">Create New Room</CardTitle>
            <CardDescription className="m-4">
              Set up a new room for your friends or colleagues to join.
              Customize your room settings and get started quickly. Share the
              room code with others to let them join.
            </CardDescription>
            <CardContent className="my-8">
              <div className="grid w-full items-center gap-4">
                <div className="flex flex-col space-y-1.5">
                  <FormField
                    control={form.control}
                    name="roomName"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Room Name</FormLabel>
                        <FormControl>
                          <Input
                            {...field}
                            placeholder="Enter Room Name"
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
                Create
              </Button>
            </CardFooter>
          </Card>
        </form>
      </Form>
    </div>
  );
}

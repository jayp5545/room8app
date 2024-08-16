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
const joinRoomCode = z.object({
  joinRoomCode: z
    .string()
    .length(5, { message: "Room code must be exactly 5 digits long" })
    .regex(/^\d{5}$/, { message: "Room code must only contain digits" }),
});

export default function JoinRoomFormCard() {
  const router = useRouter();
  const { token, setRoom } = useAuth();

  async function onSubmit(data: z.infer<typeof joinRoomCode>) {
    const BACKEND_API = config.apiBaseUrl;
    const BACKEND_ROOM_JOIN_API = BACKEND_API + "/api/v1/room/join";
    const requestBody = {
      joinCode: data.joinRoomCode,
    };
    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };

    await axios
      .post(BACKEND_ROOM_JOIN_API, requestBody, {
        validateStatus: function (status) {
          return status === 200 || status === 400;
        },
        headers: headers,
      })
      .then((response) => {
        const status_code = response.status;
        if (status_code === 200) {
          setRoom(response.data.roomDTOResponse);
          router.replace("/");
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
  const form = useForm<z.infer<typeof joinRoomCode>>({
    resolver: zodResolver(joinRoomCode),
    defaultValues: {
      joinRoomCode: "",
    },
  });

  return (
    <div
      className={cn("flex items-center justify-center h-[calc(100vh-10rem)]")}
    >
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)}>
          <Card className="w-[18rem] lg:w-[30rem] py-6 px-2">
            <CardTitle className="m-4">Join Room</CardTitle>
            <CardDescription className="m-4">
              Enter the 5-digit room code to join a room. Connect with friends
              or colleagues instantly.
            </CardDescription>
            <CardContent className="my-8">
              <div className="grid w-full items-center gap-4">
                <div className="flex flex-col space-y-1.5">
                  <FormField
                    control={form.control}
                    name="joinRoomCode"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Code</FormLabel>
                        <FormControl>
                          <Input
                            {...field}
                            placeholder="Enter Room Code"
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
                Join
              </Button>
            </CardFooter>
          </Card>
        </form>
      </Form>
    </div>
  );
}

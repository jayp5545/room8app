"use client";

import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function EmptyRoomCard() {
  const router = useRouter();

  return (
    <Card className="w-full max-w-md p-6 grid gap-6">
      <div className="grid gap-2">
        <h3 className="text-2xl font-semibold">Join a Room</h3>
        <p className="text-muted-foreground">
          You haven&apos;t joined a room yet. Click the button below to get
          started.
        </p>
      </div>
      <Button size="lg" onClick={() => router.push("/room/join")}>
        Join Room
      </Button>
      <Link href={"/room/create"} className="w-full">
            <Button className="w-full">Create room</Button>
          </Link>
    </Card>
  );
}

import { Card, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { RoomInfoType, UserType } from "@/types/types";
import UserAvatar from "../user-avatar";
import {
  formatDate,
  getFullNameFromUser,
  getInitialsFromUser,
} from "@/lib/utils";
import Link from "next/link";

interface RoomCardProps {
  primary_user: UserType;
  users: UserType[];
  room: RoomInfoType | null;
  className?: string;
  onLeave: () => void;
  onDelete: () => void;
}

const RoomCard: React.FC<RoomCardProps> = ({
  primary_user,
  users,
  room,
  onLeave,
  onDelete,
  className,
}) => {
  if (!room) {
    return (
      <Card className="w-full max-w-md p-4">
        <CardTitle>Room Information</CardTitle>
        <CardHeader>You are not in any room :(</CardHeader>
        <CardFooter className="flex flex-col gap-2">
          <Link href={"/room/join"} className="w-full">
            <Button variant="outline" className="w-full">
              Join room
            </Button>
          </Link>
          <Link href={"/room/create"} className="w-full">
            <Button className="w-full">Create room</Button>
          </Link>
        </CardFooter>
      </Card>
    );
  }

  return (
    <Card className="w-full max-w-md p-4 grid gap-6">
      <div className="flex items-center gap-4">
        <UserAvatar user={primary_user} />
        <div className="grid gap-1">
          <div className="text-xl font-semibold">{`${getFullNameFromUser(primary_user)} in ${room.name}`}</div>
          <div className="text-muted-foreground">{`Joined on ${formatDate(room.joinDate)}`}</div>
        </div>
      </div>
      <div className="grid gap-2">
        <div className="flex items-center justify-between">
          <div className="text-muted-foreground">Room Code</div>
          <div className="font-semibold">{room.code}</div>
        </div>
        <div className="flex items-center justify-between">
          <div className="text-muted-foreground">Room Status</div>
          <div className="font-semibold">
            {room.status ? "Active" : "Inactive"}
          </div>
        </div>
        <div className="flex items-center justify-between">
          <div className="text-muted-foreground">Members</div>
          <div className="font-semibold">{room.members}</div>
        </div>
      </div>
      <div className="grid gap-4">
        <div className="grid gap-2">
          <div className="text-muted-foreground">Members</div>
          <div className="grid gap-2">
            {users.map((user, index) => {
              return (
                <div key={index} className="flex items-center gap-4">
                  <Avatar className="bg-secondary text-secondary-foreground">
                    <AvatarFallback>{getInitialsFromUser(user)}</AvatarFallback>
                  </Avatar>
                  <div className="grid gap-1">
                    <div className="text-sm font-semibold">
                      {getFullNameFromUser(user)}
                    </div>
                    <div className="text-xs text-muted-foreground">
                      {user.email}
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
        <div className="flex flex-col gap-2">
          <Button
            variant="outline"
            className="w-full hover:bg-destructive/5"
            onClick={onLeave}
          >
            Leave Room
          </Button>
          <Button variant="destructive" className="w-full" onClick={onDelete}>
            Delete Room
          </Button>
        </div>
      </div>
    </Card>
  );
};

export default RoomCard;

import React from "react";
import { Card } from "@/components/ui/card";
import { UserType } from "@/types/types";
import { cn, getFullNameFromUser } from "@/lib/utils";
import UserAvatar from "../user-avatar";
import EditUserModal from "./edit-room-modal";

interface UserCardProps {
  user: UserType;
  className?: string;
  onUpdateUser: (updatedUser: UserType) => void;
}

const UserCard: React.FC<UserCardProps> = ({
  user,
  className = "",
  onUpdateUser,
}) => {
  return (
    <Card className={cn("w-full max-w-sm p-6 grid gap-6", className)}>
      <div className="flex items-center justify-between gap-4">
        <div className="flex gap-4 items-center">
          <UserAvatar user={user} />
          <div className="flex flex-col">
            <div className="text-xl font-semibold">
              {getFullNameFromUser(user)}
            </div>
            <div className="text-muted-foreground">{user.email}</div>
          </div>
        </div>
        <EditUserModal user={user} onSave={onUpdateUser} />
      </div>
    </Card>
  );
};

export default UserCard;

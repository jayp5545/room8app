import { getInitialsFromUser } from "@/lib/utils";
import { Avatar, AvatarFallback } from "./ui/avatar";
import { UserType } from "@/types/types";

interface UserAvatarProp {
  user: UserType;
}

const UserAvatar: React.FC<UserAvatarProp> = ({ user }) => {
  return (
    <Avatar>
      <AvatarFallback>{getInitialsFromUser(user)}</AvatarFallback>
    </Avatar>
  );
};

export default UserAvatar;

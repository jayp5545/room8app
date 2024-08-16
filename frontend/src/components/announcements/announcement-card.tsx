// @ts-nocheck

import React from "react";
import { Card, CardHeader, CardContent } from "@/components/ui/card";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
} from "@/components/ui/dropdown-menu";
import {
  MoveHorizontal as MoveHorizontalIcon,
  FilePen as FilePenIcon,
  Trash as TrashIcon,
} from "lucide-react";
import { EditAnnouncementDialog } from "./dialog/edit-announcement-dialog";
import { DeleteAnnouncementDialog } from "./dialog/delete-announcement-dialog";
import { AnnouncementType } from "@/types/types";
import { formatDate, getInitials } from "@/lib/utils";
import { useAuth } from "@/context/AuthContext";
import axios from "axios";
import config from "@/config";
import { toast } from "../ui/use-toast";

interface AnnouncementCardProps extends AnnouncementType {
  refreshAnnouncements: () => void;
}

const AnnouncementCard: React.FC<AnnouncementCardProps> = ({
  id,
  title,
  description,
  userPostedFirstName,
  userPostedLastName,
  userModifiedFirstName,
  userModifiedLastName,
  postedDateTime,
  lastUpdatedDateTime,
  refreshAnnouncements,
}) => {
  const { token } = useAuth();
  const announcement: AnnouncementType = {
    id,
    title,
    description,
    userPostedFirstName,
    userPostedLastName,
    userModifiedFirstName,
    userModifiedLastName,
    postedDateTime,
    lastUpdatedDateTime,
  };

  const handleEdit = async (id: string, title: string, description: string) => {
    const BACKEND_API = config.apiBaseUrl;
    const EDIT_ANNOUNCEMENT_API = BACKEND_API + `/api/v1/announcements/update/${id}`;

    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };

    const body = {
      title: title,
      description: description,
    };

    await axios
      .put(EDIT_ANNOUNCEMENT_API, body, {
        headers: headers,
        validateStatus: (status) => {
          return status === 200 || status === 400;
        },
      })
      .then((response) => {
        const status = response.status;
        if (status === 200) {
          toast({
            title: response.data?.title,
            description: "Edited successfully",
          });
        }
        if (status === 400) {
          toast({
            title: response.data?.errorMessage,
          });
        }
      })
      .catch((error) => {
        console.error("Error editing the announcement", error);
        toast({
          title: "Failed to edit the announcement",
          description: "Please try again later",
        });
      });

    refreshAnnouncements();
  };

  const handleDelete = async (id: string) => {
    const BACKEND_API = config.apiBaseUrl;
    const DELETE_ANNOUNCEMENT_API = BACKEND_API + `/api/v1/announcements/delete/${id}`;

    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };

    const body = {
      title: title,
      description: description,
    };

    await axios
      .delete(DELETE_ANNOUNCEMENT_API, {data: body})
      .then((response) => {
        const status = response.status;
        if (status === 200) {
          console.log("in correct place bro")
          toast({
            title: response.data?.title,
            description: "Annoncement Deleted successfully",
          });
        }
        if (status === 400) {
          toast({
            title: response.data?.errorMessage,
          });
        }
      })
      .catch((error) => {
        console.error("Error deleting the announcement", error);
        toast({
          title: "Failed to delete the announcement",
          description: "Please try again later",
        });
      });

    refreshAnnouncements();
  };

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Avatar className="w-8 h-8">
              <AvatarFallback>
                {getInitials(userPostedFirstName, userPostedLastName)}
              </AvatarFallback>
            </Avatar>
            <div className="grid gap-0.5">
              <p className="text-sm font-medium">{`${userPostedFirstName} ${userPostedLastName}`}</p>
              <p className="text-xs text-muted-foreground">
                Posted on {formatDate(postedDateTime)}
              </p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <p className="text-xs text-muted-foreground">
              Last updated on {formatDate(lastUpdatedDateTime)}
            </p>
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="ghost" size="icon">
                  <MoveHorizontalIcon className="w-4 h-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                <EditAnnouncementDialog
                  announcement={announcement}
                  onEdit={handleEdit}
                >
                  <DropdownMenuItem onSelect={(e) => e.preventDefault()}>
                    <FilePenIcon className="w-4 h-4 mr-2" />
                    Edit
                  </DropdownMenuItem>
                </EditAnnouncementDialog>
                <DeleteAnnouncementDialog
                  announcementId={id}
                  announcementTitle={title}
                  onDelete={handleDelete}
                >
                  <DropdownMenuItem
                    onSelect={(e) => e.preventDefault()}
                    className="text-destructive"
                  >
                    <TrashIcon className="w-4 h-4 mr-2" />
                    Delete
                  </DropdownMenuItem>
                </DeleteAnnouncementDialog>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        <div className="space-y-2">
          <h3 className="text-lg font-medium">{title}</h3>
          <p>{description}</p>
        </div>
      </CardContent>
    </Card>
  );
};

export default AnnouncementCard;

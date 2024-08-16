import React, { useState, ReactNode } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { AnnouncementType } from "@/types/types";

interface EditAnnouncementDialogProps {
  announcement: AnnouncementType;
  onEdit: (id: string, title: string, description: string) => void;
  children: ReactNode;
}

export const EditAnnouncementDialog: React.FC<EditAnnouncementDialogProps> = ({
  announcement,
  onEdit,
  children,
}) => {
  const [title, setTitle] = useState(announcement.title);
  const [description, setDescription] = useState(announcement.description);
  const [open, setOpen] = useState(false);

  const handleSubmit = () => {
    onEdit(announcement.id, title, description);
    setOpen(false);
  };

  const handleTriggerClick = (e: React.MouseEvent) => {
    e.preventDefault();
    setOpen(true);
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <div onClick={handleTriggerClick}>{children}</div>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Edit Announcement</DialogTitle>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="title" className="text-right">
              Title
            </Label>
            <Input
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="col-span-3"
            />
          </div>
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="description" className="text-right">
              Description
            </Label>
            <Textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="col-span-3"
            />
          </div>
        </div>
        <DialogFooter>
          <Button type="submit" onClick={handleSubmit}>
            Save Changes
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

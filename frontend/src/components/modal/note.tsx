// @ts-nocheck

import React, { useState } from "react";

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Label } from "@/components/ui/label";
import { Button } from "../ui/button";



export function CreateNoteModal({note,setShowNote}) {

    console.log("inside")

  const [isOpen, setIsOpen] = useState(true);

  const handleClick = () => {
    setShowNote(pre => !pre)
    setIsOpen(false);
  }

  return (
      <Dialog open={isOpen} onOpenChange={setIsOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Note</DialogTitle>
            <DialogDescription>
             {note}
            </DialogDescription>
          </DialogHeader>
          <Button onClick={handleClick}>Close</Button>
        </DialogContent>
      </Dialog>
  );
}

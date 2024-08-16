// @ts-nocheck

import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import { Toaster } from "@/components/ui/toaster";

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { z } from "zod";
import { useToast } from "@/components/ui/use-toast";
import { useAuth } from "@/context/AuthContext";
import config from "@/config";

const listSchema = z.object({
  name: z.string().min(1, { message: "Name is required" }),
});

export function CreateListModal({ fetchData }) {
  const [name, setName] = useState("");
  const [errors, setErrors] = useState({});
  const [isOpen, setIsOpen] = useState(false);
  const { toast } = useToast();
  const { token } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});

    try {
      listSchema.parse({ name });

      const response = await fetch(
        `${config.apiBaseUrl}/api/v1/grocery-list/add`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ name }),
        },
      );

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || "Failed to create the list");
      }

      setName("");
      fetchData();
      toast({
        title: "Success",
        description: data.message || "List created successfully",
        status: "success",
      });
      setIsOpen(false);
    } catch (err) {
      if (err instanceof z.ZodError) {
        setErrors(err.formErrors.fieldErrors);
      } else {
        toast({
          title: "Error",
          description: err.message,
          status: "error",
        });
      }
    }
  };

  return (
    <>
      <Dialog open={isOpen} onOpenChange={setIsOpen}>
        <DialogTrigger asChild>
          <Button onClick={() => setIsOpen(true)}>Create List</Button>
        </DialogTrigger>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Create List</DialogTitle>
            <DialogDescription>
              Create list by filling below fields
            </DialogDescription>
          </DialogHeader>
          <form onSubmit={handleSubmit}>
            <div className="grid gap-4 py-4">
              <div className="grid grid-cols-4 items-center gap-4">
                <Label htmlFor="name" className="text-right">
                  Name
                </Label>
                <Input
                  id="name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  className="col-span-3"
                />
                {errors.name && (
                  <div className="text-red-500 col-span-4">
                    {errors.name[0]}
                  </div>
                )}
              </div>
            </div>
            <DialogFooter>
              <Button type="submit">Create</Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
      <Toaster />
    </>
  );
}

// @ts-nocheck

import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import { Toaster } from "@/components/ui/toaster";
import { useParams, useSearchParams } from "next/navigation";

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
import axios from "axios";

const itemSchema = z.object({
  name: z.string().min(1, { message: "Name is required" }),
  quantity: z.string().refine((val) => !Number.isNaN(parseInt(val, 10)), {
    message: "Expected number, received a string",
  }),
  note: z.string().min(1, { message: "Note is required" }),
});

export function CreateItemModal({ fetchData }) {
  const params = useParams();
  const search = useSearchParams();
  const groceryListName = decodeURI(search.get('name'));

  const [name, setName] = useState("");
  const [quantity, setQuantity] = useState("");
  const [note, setNote] = useState("");
  const [errors, setErrors] = useState({});
  const [isOpen, setIsOpen] = useState(false);
  const { toast } = useToast();
  const { token } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    try {
      itemSchema.parse({ name, quantity, note });

      const response = await axios.post(
        `${config.apiBaseUrl}/api/v1/grocery-list/item/add`,
        { name, quantity, note, groceryListName },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );

      setName("");
      setQuantity("");
      setNote("");
      fetchData();
      toast({
        title: "Success",
        description: response.data.message || "Item added successfully",
        status: "success",
      });
      setIsOpen(false);
    } catch (err) {
      if (err instanceof z.ZodError) {
        console.log("ERRORRR", err.formErrors.fieldErrors);
        setErrors(err.formErrors.fieldErrors);
      } else if (axios.isAxiosError(err)) {
        toast({
          title: "Error",
          description: err.response?.data?.message || "Failed to add the item",
          status: "error",
        });
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
          <Button onClick={() => setIsOpen(true)}>Create Item</Button>
        </DialogTrigger>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Create Item</DialogTitle>
            <DialogDescription>
              Create Item by filling below fields
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
              <div className="grid grid-cols-4 items-center gap-4">
                <Label htmlFor="name" className="text-right">
                  Quantity
                </Label>
                <Input
                  id="quantity"
                  value={quantity}
                  onChange={(e) => setQuantity(e.target.value)}
                  className="col-span-3"
                  type="number"
                />
                {errors.quantity && (
                  <div className="text-red-500 col-span-4">
                    {errors.quantity[0]}
                  </div>
                )}
              </div>
              <div className="grid grid-cols-4 items-center gap-4">
                <Label htmlFor="name" className="text-right">
                  Note
                </Label>
                <Input
                  id="note"
                  value={note}
                  onChange={(e) => setNote(e.target.value)}
                  className="col-span-3"
                />
                {errors.note && (
                  <div className="text-red-500 col-span-4">
                    {errors.note[0]}
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

// @ts-nocheck

"use client";

import { useState, useEffect, useReducer } from "react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { CreateItemModal } from "../modal/createItem";
import { useToast } from "@/components/ui/use-toast";
import { LucideTrash2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { useParams } from "next/navigation";
import { CreateNoteModal } from "../modal/note";
import { useAuth } from "@/context/AuthContext";
import config from "@/config";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";

export function GroceryItem() {
  const [items, setItems] = useState([]);
  const [showNote, setShowNote] = useState(-1);
  const { toast } = useToast();
  const params = useParams();
  const { token } = useAuth();

  const [pendingDialogOpen, setPendingDialogOpen] = useState(false);

  async function fetchData() {
    try {
      const response = await fetch(
        `${config.apiBaseUrl}/api/v1/grocery-list/item/get/all/${params.name}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        },
      );
      const data = await response.json();
      console.log(data);
      console.log(data[0].grocery_items);
      setItems(data);
    } catch (error) {
      console.error("Error fetching grocery lists:", error);
    }
  }

  async function handleDelete(listID) {
    console.log({ listID });
    try {
      const response = await fetch(
        `${config.apiBaseUrl}/api/v1/grocery-list/item/delete/${listID}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        },
      );

      if (!response.ok) {
        throw new Error("Failed to delete the list");
      }
      console.log({ showNote });

      toast({
        title: "Success",
        description: "Item deleted successfully",
        status: "success",
      });

      fetchData();
    } catch (error) {
      toast({
        title: "Error",
        description: error.message,
        status: "error",
      });
    }
  }

  const handleClick = (idx) => {
    setShowNote(idx);
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className="bg-background text-foreground min-h-screen flex flex-col w-full">
      <header className="bg-muted p-4 flex justify-end z-50">
        <CreateItemModal fetchData={fetchData} />
      </header>
      <main className="flex-1 overflow-auto p-6">
        <div className="grid gap-6">
          {items &&
            items.map((item, idx) => (
              <>
                <div onClick={() => handleClick(idx)}>
                  <Card
                    key={item.id}
                    className="bg-card text-card-foreground flex items-center justify-between p-3 cursor-pointer hover:bg-primary-foreground"
                  >
                    <div className="flex items-center gap-4 ml-5 w-4/12">
                      <h3 className="text-3xl font-semibold">{item.name}</h3>
                      <Badge
                        style={{
                          backgroundColor:
                            item.purchased.toString() === "true"
                              ? "green"
                              : "red",
                        }}
                        className="ml-auto"
                        onClick={() => !item.purchased && setPendingDialogOpen(item.id)}
                      >
                        {item.purchased.toString() === "true"
                          ? "Purchased"
                          : "Pending"}
                      </Badge>
                    </div>
                    <div className="flex flex-col gap-2 text-sm text-muted-foreground">
                      <div>
                        <span className="font-semibold">Last modified:</span>{" "}
                        {item.last_modified_on} by{" "}
                        {item.last_modified_by.firstName}{" "}
                        {item.last_modified_by.lastName}
                      </div>
                      <div>
                        <span className="font-semibold">Created:</span>{" "}
                        {item.added_on} by {item.added_by.firstName}{" "}
                        {item.added_by.lastName}
                      </div>
                    </div>
                    <div className="flex mr-5 items-center gap-4">
                      <div className="text-3xl font-semibold">
                        {item.quantity}
                      </div>
                      <Button
                        variant="ghost"
                        size="icon"
                        onClick={() => handleDelete(item.id)}
                      >
                        <LucideTrash2 className="text-red-500 rounded-full " />
                      </Button>
                    </div>
                  </Card>
                </div>
                <Dialog
                  open={showNote === idx}
                  onOpenChange={(open) => !open && setShowNote(-1)}
                >
                  <DialogContent key={item.id} className="sm:max-w-[425px]">
                    <DialogHeader>
                      <DialogTitle>Note</DialogTitle>
                      <DialogDescription>{item.note}</DialogDescription>
                    </DialogHeader>
                  </DialogContent>
                </Dialog>
              </>
            ))}
        </div>

       {pendingDialogOpen >0 && <AlertDialog open={pendingDialogOpen > 0}>
        <AlertDialogContent className="lg:max-w-screen-lg overflow-y-scroll max-h-screen">
          <AlertDialogHeader>
            <AlertDialogTitle>Are you sure?</AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone. This will permanently delete your item
              and remove your data from our servers.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel onClick={() => setPendingDialogOpen(false)}>Cancel</AlertDialogCancel>
            <AlertDialogAction onClick={async () => {
              const item = items.find(i => i.id === pendingDialogOpen)
              const response = await fetch(
                `${config.apiBaseUrl}/api/v1/grocery-list/item/update/purchased`,
                {
                  method: "PUT",
                  headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                  },
                  body: JSON.stringify({
                    ...item
                  })
                },
              );
              setPendingDialogOpen(false);
              setItems(items.map(i => {
                return {
                  ...i,
                  purchased: pendingDialogOpen === i.id ? true : i.purchased
                }
              }))
            }}>Continue</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>}

      </main>
    </div>
  );
}

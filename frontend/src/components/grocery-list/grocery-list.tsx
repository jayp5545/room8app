// @ts-nocheck

"use client";

import { useState, useEffect, useReducer } from "react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { CreateListModal } from "../modal/createList";
import { useToast } from "@/components/ui/use-toast";
import { LucideTrash2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import EmptyRoomCard from "../empty-room";
import config from "@/config";

export function GroceryList() {
  const [lists, setLists] = useState([]);
  const { toast } = useToast();
  const router = useRouter();
  const { token } = useAuth();
  const { email } = useAuth();

  async function fetchData() {
    try {
      const response = await fetch(
        `${config.apiBaseUrl}/api/v1/grocery-list/get/all`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        },
      );
      const data = await response.json();
      setLists(data);
    } catch (error) {
      console.error("Error fetching grocery lists:", error);
    }
  }

  async function handleDelete(id) {
    try {
      const response = await fetch(
        `${config.apiBaseUrl}/api/v1/grocery-list/delete/${id}`,
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

      toast({
        title: "Success",
        description: "List deleted successfully",
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

  const handleClick = (id, name) => {
    router.push(`/groceryItem/${id}?name=${name}`);
  };

  useEffect(() => {
    if (token) {
      fetchData();
    }
  }, [token]);

  console.log(lists)

  return (
    <div className="bg-background text-foreground min-h-screen flex flex-col w-full">
      <header className="px-8 flex justify-end">
        <CreateListModal fetchData={fetchData} />
      </header>
      <main className="flex-1 overflow-auto p-6">
        <div className="grid gap-6">
          {lists?.map &&
            lists.map((list) => (
              <div key={list.id} onClick={() => handleClick(list.id, list.name)}>
                <Card
                  key={list.id}
                  className="bg-card text-card-foreground flex items-center justify-between p-3 cursor-pointer hover:bg-accent"
                >
                  <div className="flex items-center gap-4 ml-5 w-4/12">
                    <h3 className="text-3xl font-semibold">{list.name}</h3>
                    {/* <Badge
                      style={{
                        backgroundColor:
                          list.active.toString() === "true" ? "green" : "red",
                      }}
                      className="ml-auto"
                    >
                      {list.active.toString() === "true"
                        ? "Active"
                        : "Inactive"}
                    </Badge> */}
                  </div>
                  <div className="flex flex-col gap-2 text-sm text-muted-foreground">
                    <div>
                      <span className="font-semibold">Last modified:</span>{" "}
                      {list.last_modified_on} by{" "}
                      {list.last_modified_by.firstName}{" "}
                      {list.last_modified_by.lastName}
                    </div>
                    <div>
                      <span className="font-semibold">Created:</span>{" "}
                      {list.date_of_creation} by {list.created_by.firstName}{" "}
                      {list.created_by.lastName}
                    </div>
                  </div>
                  <div className="flex mr-5 items-center gap-4">
                    <div className="text-3xl font-semibold">
                      {list.items_purchased}/{list.items}
                    </div>
                    <Button
                      variant="ghost"
                      size="icon"
                      onClick={(e) => {
                        e.stopPropagation();
                        handleDelete(list.id);
                      }}
                    >
                      <LucideTrash2 className="text-red-500 rounded-full " />
                    </Button>
                  </div>
                </Card>
              </div>
            ))}
        </div>
      </main>
    </div>
  );
}

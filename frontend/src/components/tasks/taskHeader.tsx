import React, { useState, useEffect } from 'react';
import { format } from "date-fns";
import { Calendar as CalendarIcon } from "lucide-react";
import { Calendar } from "@/components/ui/calendar";
import { cn } from "@/lib/utils";
import config from "@/config";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuRadioGroup,
  DropdownMenuRadioItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useAuth } from "@/context/AuthContext";
import { useToast } from "@/components/ui/use-toast";
import axios from "axios";

interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}

export default function TaskHeader() {
  const { token } = useAuth();
  const { toast } = useToast();

  const [showAddModal, setShowAddModal] = useState(false);
  const [taskName, setTaskName] = useState("");
  const [description, setDescription] = useState("");
  const [date, setDate] = useState<Date | undefined>();
  const [assignedTo, setAssignedTo] = useState<string>("");
  const [users, setUsers] = useState<User[]>([]);
  const [isCalendarOpen, setIsCalendarOpen] = useState(false);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get(`${config.apiBaseUrl}/api/v1/room/get/all/users`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const allUsers = await response.data;
      if (Array.isArray(allUsers)) {
        setUsers(allUsers);
      } else {
        console.error("API did not return an array of users:", allUsers);
        setUsers([]);
      }
      setUsers(allUsers);
    } catch (error) {
      console.error("Failed to fetch users:", error);
      toast({
        title: "Error",
        description: "Failed to load users. Please try again.",
      });
    }
  };

  const handleAddSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    const body = {
      name: taskName,
      description: description,
      taskDate: date ? format(date, "yyyy-MM-dd") : null,
      assignedTO: assignedTo,
    };
    e.preventDefault();
    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };
    try {
      const res = await axios.post(`${config.apiBaseUrl}/task/add`, body, {
        headers,
        validateStatus: (status) => status === 200 || status === 400,
      });

      if (res.status === 400) {
        toast({
          title: "Error",
          description: res.data?.errorMessage || "Failed to add task",
        });
        return;
      }

      toast({
        title: "Success",
        description: "Task added successfully",
      });
      setShowAddModal(false);
      setTaskName("");
      setDescription("");
      setDate(undefined);
      setAssignedTo("");
    } catch (error) {
      toast({
        title: "Error",
        description: "An unexpected error occurred",
      });
    }
  };

  return (
    <div className="flex justify-between items-center">
      <div>
        <h1 className="text-4xl font-bold mb-10">Tasks</h1>
      </div>
      <div>
        <Button onClick={() => setShowAddModal(true)} variant="outline">Add Task</Button>
      </div>

      <Dialog open={showAddModal} onOpenChange={setShowAddModal}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle className="text-2xl">Add Task</DialogTitle>
            <DialogDescription className="py-4">Please add all the details of the Task.</DialogDescription>
          </DialogHeader>
          <form onSubmit={handleAddSubmit} className="space-y-4">
            <div>
              <Label htmlFor="taskName">Task Name</Label>
              <Input
                id="taskName"
                value={taskName}
                onChange={(e) => setTaskName(e.target.value)}
                required
              />
            </div>
            <div>
              <Label htmlFor="description">Description</Label>
              <Textarea
                id="description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                className="min-h-[100px]"
              />
            </div>
            <div className="relative">
              <Label htmlFor="date">Select the Task Due Date</Label>
              <Button
                id="date"
                type="button"
                variant={"outline"}
                className={cn(
                  "w-full justify-start text-left font-normal",
                  !date && "text-muted-foreground"
                )}
                onClick={() => setIsCalendarOpen(!isCalendarOpen)}
              >
                <CalendarIcon className="mr-2 h-4 w-4" />
                {date ? format(date, "PPP") : <span>Pick a date</span>}
              </Button>
              {isCalendarOpen && (
                <div className="absolute z-10 mt-1 bg-white border rounded-md shadow-lg">
                  <Calendar
                    mode="single"
                    selected={date}
                    onSelect={(newDate) => {
                      setDate(newDate);
                      setIsCalendarOpen(false);
                    }}
                    initialFocus
                  />
                </div>
              )}
            </div>
            <div>
              <Label htmlFor="assignedTo">Assigned To</Label>
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="outline" className="w-full text-left">
                    {assignedTo
                      ? users.find((user) => user.email === assignedTo)?.firstName + ' ' + users.find((user) => user.email === assignedTo)?.lastName
                      : "Select User"}
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent className="w-full">
                  <DropdownMenuRadioGroup value={assignedTo} onValueChange={setAssignedTo}>
                    {users.map((user) => (
                      <DropdownMenuRadioItem key={user.email} value={user.email}>
                        {user.firstName} {user.lastName}
                      </DropdownMenuRadioItem>
                    ))}
                  </DropdownMenuRadioGroup>
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
            <DialogFooter>
              <Button type="submit">Add Task</Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}
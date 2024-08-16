// @ts-nocheck

import { ImCross } from "react-icons/im";
import { LuPencilLine } from "react-icons/lu";
import { Plus ,Pencil, Trash2, SearchX, CloudFog} from "lucide-react"
import { FaUser } from "react-icons/fa";
import { TaskType } from "@/types/types";
import { useAuth } from "@/context/AuthContext";
import { useToast } from "@/components/ui/use-toast";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { format } from "date-fns"
import { Calendar as CalendarIcon } from "lucide-react"
import { Calendar } from "@/components/ui/calendar"
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"

import { cn } from "@/lib/utils"
import config from "@/config";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
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

interface TaskComponentProps {
  task: TaskType;
  key: number;
  fetchData: any;
}

export default function TaskComponent({ task, fetchData }: TaskComponentProps) {
  const { token } = useAuth();
  const { toast } = useToast();
  const router = useRouter();
  const [showAddModal, setShowAddModal] = useState(false);

  const [date, setDate] = useState<Date>()
  const [deleteClick, setDeleteClick] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const handleDelete = () => {
    setDeleteClick(true);
    removeTask();
  };

  async function removeTask() {
    try {
      const response = await fetch(`${config.apiBaseUrl}/task/delete?id=${task.id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error("Failed to delete the list");
      }

      toast({
        title: "Success",
        description: "Item deleted successfully",
      });

      setDeleteClick(false);
      fetchData();
    } catch (error) {
      toast({
        title: "Error",
        description: error.message,
      });
    }
  }


  const handleAddSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    console.log("in add")
    e.preventDefault();
    console.log("first");
    console.log(description);
    console.log(amount)
    console.log(paidBy);
    console.log(participantEmails)
    console.log(JSON.stringify({
      description,
      amount:
      paidBy,
      participantEmails
    }));
    try {
      const addUrl = `${config.apiBaseUrl}/api/v1/expense/add`;
      console.log("first")
      console.log(addUrl)
      const response = await fetch(addUrl, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          description,
          amount,
          paidBy,
          participantEmails
        }),
      });
      
      const newExpense = await response.json();
      groupedExpenses.push(newExpense);
      setShowAddModal(false);
      await fetchTransactionsData(filterStatus);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className="px-10 py-4">
        <Card key={task.name} className="">
          <CardHeader className="flex justify-between items-start">
            <div className="w-full">
              <div className="flex justify-between">
                <CardTitle className="text-3xl">{task.name}</CardTitle>
                <div className="flex gap-6 h-100">
                  <Pencil className="cursor-pointer" onClick={() => handleEditClick(announcement)} />
                  <Trash2 className="cursor-pointer" onClick={() => handleDelete()} />
                </div>
             
              </div>
              <CardDescription className=" py-5 text-xl">
                {task.description}
              </CardDescription>
            </div>
         
          </CardHeader>

          <CardContent>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <p className="text-lg  font-bold">Assigned to</p>
                <p className="   ">{task.assignedTo.firstName} {task.assignedTo.lastName}</p>
              </div>
              {/* <div>
                <p className="text-lg  font-bold">Last modified by</p>
                <p className=" ">{announcement.userModifiedFirstName} {announcement.userModifiedLastName}</p>
              </div> */}
              <div>
                <p className="text-lg  font-bold">Created</p>
                <p className="text-gray-600">
                {new Date(task.taskDate).toLocaleDateString('en-US', {
                  year: 'numeric',
                  month: '2-digit',
                  day: '2-digit'
                })} 
                {/* {' '} 
                {new Date(announcement.postedDateTime).toLocaleTimeString('en-US', {
                  hour: '2-digit',
                  minute: '2-digit',
                  hour12: true
                })} */}
                </p>
              </div>
              {/* <div>
                <p className="text-lg font-bold">Last modified</p>
                <p className="text-gray-600">
                {task.lastUpdatedDateTime  ? 
                new Date(task.lastUpdatedDateTime).toLocaleDateString('en-US', {
                  year: 'numeric',
                  month: '2-digit',
                  day: '2-digit'
                }):     '-'
                } 
                {' '} 
                {task.lastUpdatedDateTime  ?  
                new Date(task.lastUpdatedDateTime).toLocaleTimeString('en-US', {
                  hour: '2-digit',
                  minute: '2-digit',
                  hour12: true
                }) : ''
              }
                </p>
              </div> */}
            </div>
          </CardContent>
          
        </Card>


      <AlertDialog open={deleteClick}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Are you sure?</AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone. This will permanently delete your task
              and remove your data from our servers.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel onClick={() => setDeleteClick(false)}>Cancel</AlertDialogCancel>
            <AlertDialogAction onClick={removeTask}>Continue</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>



      {/* <Dialog open={showAddModal} onOpenChange={setShowAddModal}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle className="text-2xl">Add Task</DialogTitle>
            <DialogDescription className="py-4">Please add all the details of the Task.</DialogDescription>
          </DialogHeader>
          <form onSubmit={handleAddSubmit} className="space-y-4">
            <div>
              <Label htmlFor="taskName">Task Name</Label>
              <Textarea
                id="taskName"
                value={taskName}
                onChange={(e) => setDescription(e.target.value)}
                required
                className="min-h-[100px] py-3"
              />
            </div>
             <Label htmlFor="amount" className="text-right">Amount</Label>
              <Input
                id="amount"
                // type="number"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                className="col-span-3"
              /> 
<Label htmlFor="date">Select the Task Due Date</Label>
<Popover>
      <PopoverTrigger asChild>
        <Button
          variant={"outline"}
          className={cn(
            "w-[280px] justify-start text-left font-normal",
            !date && "text-muted-foreground"
          )}
        >
          <CalendarIcon className="mr-2 h-4 w-4" />
          {date ? format(date, "PPP") : <span>Pick a date</span>}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-auto p-0">
        <Calendar
          mode="single"
          selected={date}
          onSelect={setDate}
          initialFocus
        />
      </PopoverContent>
    </Popover>

            <Label className="mt-4">Paid By</Label>
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="outline" className="w-full text-left">
                  {paidBy
                    ? users.find((user) => user.email === paidBy)?.firstName + ' ' + users.find((user) => user.email === paidBy)?.lastName ||                      "Select User"
                    : "Select User"}
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent className="w-full">
                <DropdownMenuRadioGroup value={paidBy} onValueChange={setPaidBy}>
                  {users.map((user) => (
                    <DropdownMenuRadioItem key={user.email} value={user.email}>
                      {user.firstName} {user.lastName}
                    </DropdownMenuRadioItem>
                  ))}
                </DropdownMenuRadioGroup>
              </DropdownMenuContent>
            </DropdownMenu>

            <Label className="mt-4">Participants</Label>
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="outline" className="w-full">
                  {participantEmails.length > 0
                    ? `${participantEmails.length} Participants Selected`
                    : "Select Participants"}
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent className="w-full">
                {users.map((user) => (
                  <DropdownMenuCheckboxItem
                    key={user.email}
                    checked={participantEmails.includes(user.email)}
                    onCheckedChange={(checked) => {
                      setParticipantEmails((prev) =>
                        checked
                          ? [...prev, user.email]
                          : prev.filter((email) => email !== user.email)
                      );
                    }}
                  >
                    {user.firstName} {user.lastName}
                  </DropdownMenuCheckboxItem>
                ))}
              </DropdownMenuContent>
            </DropdownMenu>


            <DialogFooter>
              <Button type="submit">
                Add Expense
              </Button>
      
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog> */}
    </div>
  );
}
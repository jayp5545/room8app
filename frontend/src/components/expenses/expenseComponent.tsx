// @ts-nocheck

"use client";
import React, { useEffect, useState, useCallback } from "react";

import { useAuth } from "@/context/AuthContext";
import { Trash2 } from "lucide-react";
import { ChevronDown } from "lucide-react";
import { Button } from "../ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog"
import { Textarea } from "@/components/ui/textarea"
import { Label } from "@/components/ui/label"
import { Pencil,Plus } from 'lucide-react';
import Link from 'next/link';
import config from "@/config";  

import { Input } from "@/components/ui/input"

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuCheckboxItem,
  DropdownMenuRadioGroup,
  DropdownMenuRadioItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}
interface Room {
  id: number;
  name: string;
  members: number;
  code: number;
  active: boolean;
}

interface Expense {
  id: number;
  room: Room;
  description: string;
  amount: number;
  paidBy: User;
  participants: {
    id: number;
    user: User;
    amount: number;
  }[];
  status: boolean;
  dateOfCreation: string;
  lastModifiedOn: string;
  createdBy: User;
  last_modified_by: User;
}

interface Payment {
  id: number;
  room: Room;
  paidBy: User;
  paidTo: User;
  amount: number;
  status: boolean;
  dateOfCreation: string;
  lastModifiedOn: string;
  createdBy: User;
  last_modified_by: User;
}
type TransactionItem = (Expense | Payment) & { type: "expense" | "payment" };

const ExpenseComponent = () => {
  const { token, user } = useAuth();
  const [showAddModal, setShowAddModal] = useState(false);

  

  const [expenses, setExpenses] = useState<Expense[]>([]);

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [paidBy, setPaidBy] = useState<string>("");
  const [participantEmails, setParticipantEmails] = useState<string[]>([]);
  const [users,setUsers] = useState<User[]>([]);

  const [filterStatus, setFilterStatus] = useState("all");

  const [transactions, setTransactions] = useState<TransactionItem[]>([]);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [amount, setAmount] = useState("");
  const [showUpdateModal, setShowUpdateModal] = useState(false);

  const isUserParticipant = (participants: any[], email: string) => {
    return participants.some((participant) => participant.user.email === email);
  };

  const isUserLent = (paidByEmail: string) => {
    return paidByEmail === user?.email;
  };

 const fetchUsers = async () => {
  try {
    const response = await fetch(
      `${config.apiBaseUrl}/api/v1/room/get/all/users`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      })
      const allUsers = await response.json();
      if (Array.isArray(allUsers)) {
        setUsers(allUsers);
      } else {
        console.error("API did not return an array of users:", allUsers);
        setUsers([]);
      }
      // setUsers(allUsers);
 }catch (err) {
    console.log(err);
  }
}


  
  const formatDate = (dateString: String) => {
    const date = new Date(dateString.toString());
    return `${date.getDate().toString().padStart(2, "0")} ${date.toLocaleString(
      "default",
      { month: "short" },
    )}`;
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchTransactionsData = useCallback(
    async (type: String) => {
      try {
        if (type === "inactive") {
          // type = "in-active";
        }
        console.log(type);
        const url = `${config.apiBaseUrl}/api/v1/expense/get/all/${type}`;
        const expensesResponse = await fetch(

          url,
          {
            method: "GET",
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          },
        );
        const expensesData = await expensesResponse.json();
        setExpenses(expensesData);

        const url1 = `${config.apiBaseUrl}/api/v1/settleup/get/all/${type}`;
        const paymentsResponse = await fetch(
          url1,
          {
            method: "GET",
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          },
        );
        const paymentsData = await paymentsResponse.json();
        console.log("payments data");
        console.log(paymentsData);
        
        console.log({expensesData, paymentsData})

        const allTransactions = [
          ...expensesData.map((expense: Expense) => ({
            ...expense,
            type: "expense" as const,
          })),
          ...paymentsData.map((payment: Payment) => ({
            ...payment,
            type: "payment" as const,
          })),
        ].sort(
          (a, b) =>
            new Date(b.dateOfcreation).getTime() -
            new Date(a.dateOfcreation).getTime()
        );
        setTransactions(allTransactions);
      } catch (err) {
        console.log(err);
      }
    },
    [token],
  );

  useEffect(() => {
    const fetchData = async () => {
      try {
        await fetchTransactionsData(filterStatus);
      } catch (error) {
        console.error("Failed to fetch expenses:", error);
      }
    };
    if (token) {
      fetchData();
    }
  }, [token, fetchTransactionsData, filterStatus]);

  const groupTransactionsByMonth = () => {
    const grouped: { [monthYear: string]: TransactionItem[] } =
      transactions.reduce((acc, transaction) => {
        const date = new Date(transaction.dateOfCreation);
        const monthYear = `${date.toLocaleString("default", {
          month: "long",
        })} ${date.getFullYear()}`;
        if (!acc[monthYear]) {
          acc[monthYear] = [];
        }
        acc[monthYear].push(transaction);
        return acc;
      }, {} as { [key: string]: TransactionItem[] });
      console.log('here', Object.entries(grouped))
      
      return Object.entries(grouped).map(([month, values]) => {
        const sortedValues = values.sort((a, b) => {
          return new Date(b.dateOfCreation) - new Date(a.dateOfCreation);
        });

        return [month, sortedValues];
      });
  };
  const groupedExpenses = groupTransactionsByMonth();
  console.log(groupedExpenses);
  const handleDeleteClick = async (item: TransactionItem) => {
    try {
      const url =
        item.type === "expense"
          ? `${config.apiBaseUrl}/api/v1/expense/delete/${item.id}`
          : `${config.apiBaseUrl}/api/v1/settleup/delete/${item.id}`;
      const response = await fetch(url, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        setTransactions((prevTransactions) =>
          prevTransactions.filter(
            (t) => !(t.id === item.id && t.type === item.type),
          ),
        );
        if (filterStatus === "all") {
          fetchTransactionsData("all");
        }
      } else {
        console.error("Failed to delete transaction");
      }
    } catch (error) {
      console.error("Error deleting transaction:", error);
    }
  };
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

  const handleUpdateSubmit = async (e: React.FormEvent<HTMLFormElement>, itemId: number) => {
    e.preventDefault();
    try {
      const updateUrl = `${config.apiBaseUrl}/api/v1/expense/update`;
      const response = await fetch(updateUrl, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ id: itemId, description, amount, paidBy, participantEmails }),
      });
      const updatedExpense = await response.json();
      setExpenses((prev) => prev.map((expense) => (expense.id === itemId ? updatedExpense : expense)));
      setShowUpdateModal(false);
      await fetchTransactionsData(filterStatus);
    } catch (err) {
      console.log(err);
    }
  };

  const findAmountToShow = (item) => {
   //
  //  "participants": [
  //       {
  //           "id": 4,
  //           "user": {
  //               "id": 11,
  //               "firstName": "Testing2",
  //               "lastName": "Testing2",
  //               "email": "testing2@gmail.com"
  //           },
  //           "amount": 200.0
  //       },
  //       {
  //           "id": 5,
  //           "user": {
  //               "id": 12,
  //               "firstName": "testing3",
  //               "lastName": "a",
  //               "email": "testing3@gmail.com"
  //           },
  //           "amount": 200.0
  //       }
  //   ],

    if(item.paidBy.email === user?.email){
      return item.amount/(item.participants.length+1);
    }
    else{
      // return item.participants.find(participant => participant.user.email === user?.email);
      console.log(item.participants.find(participant => participant.user.email === user?.email))

      const match = item.participants.find(
        (participant) => participant.user.email === user.email
      );

      if(match){
        console.log(match)
        return match.amount; 
      }else{
        return item.amount;
      }
    }
  };

  const renderTransactionItem = (item: TransactionItem) => {
    if ("description" in item) {
      return (
        <div
          className="rounded-lg shadow-md p-4 mb-4 hover:shadow-lg transition-shadow cursor-pointer"
          key={`${item.type}-${item.id}`}
          onClick={() => { 
          
          }}
        >
          <div className="flex items-center space-x-4 py-8">
            <div className="flex flex-col items-center">
              <span className="text-xl text-gray-500">
                {formatDate(item.dateOfCreation).split(" ")[1]}
              </span>
              <span className="text-xl font-bold">
                {formatDate(item.lastModifiedOn).split(" ")[0]}
              </span>
            </div>
            <div className="flex-grow pl-6">
              <p className="text-xl font-bold">{item.description}</p>
              <p className="text-xl text-gray-500">
                {item.paidBy.firstName} paid ${item.amount.toFixed(2)}
              </p>
            </div>
            <div className="flex gap-10 text-right">
              <div className="flex gap-6 items-center justify-end">
              {item.status && (
                <>
                  <Pencil
                    className="cursor-pointer"
                    onClick={() => {
                      setDescription(item.description);
                      setAmount(item.amount.toString());
                      setPaidBy(item.paidBy.email);
                      setParticipantEmails(item.participants.map((p) => p.user.email));
                      setShowUpdateModal(item.id);
                    }}
                  />
                  <Trash2
                    className="w-6 h-6 text-red-600 cursor-pointer"
                    onClick={() => {
                      handleDeleteClick(item);
                    }}
                  />
                </>
              )}
              </div>
              <div>
                <p className="text-lg text-gray-500">
                  {user?.email && isUserLent(item.paidBy.email) && "you lent"}
                  {user?.email &&
                    isUserParticipant(item.participants, user.email) &&
                    "you borrowed"}
                  {user?.email &&
                    !isUserParticipant(item.participants, user.email) &&
                    !isUserLent(item.paidBy.email) &&
                    "You were not involved"}
                </p>
                <p
                  className={`font-semibold text-lg ${
                    item.paidBy.email == user?.email
                      ? "text-green-600"
                      : "text-red-600"
                  }`}
                >
                  {findAmountToShow(item).toFixed(2)}
                </p>
              </div>
            </div>
          </div>

          <Dialog open={showUpdateModal === item.id} onOpenChange={(open) => setShowUpdateModal(-1)}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle className="text-2xl">Update Expense</DialogTitle>
            <DialogDescription className="py-4">Update the details of the Expense.</DialogDescription>
          </DialogHeader>
          <form onSubmit={(e) => handleUpdateSubmit(e,item.id)} className="space-y-4">
            <div>
              <Label htmlFor="description">Description</Label>
              <Textarea
                id="description"
                value={description}
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
    {users
      .filter((user) => user.email !== paidBy) // Filter out the selected "Paid By" user
      .map((user) => (
        <DropdownMenuCheckboxItem
          key={user.email}
          checked={participantEmails.includes(user.email)}
          onCheckedChange={(checked) => {
            setParticipantEmails((prev) =>
              checked
                ? [...prev, user.email]
                : prev.filter((email) => email !== user.email),
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
                Update Expense
              </Button>
      
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
       
        </div>
      );
    } else {
      return (
        <div
          className="rounded-lg shadow-md p-4 mb-4 hover:shadow-lg transition-shadow cursor-pointer"
          key={item.id}
          onClick={() => {
            setIsDialogOpen(true);
          }}
        >
          <div className="flex items-center space-x-4 py-5">
            <div className="flex flex-col items-center">
              <span className="text-xl text-gray-500">
                {formatDate(item.dateOfCreation).split(" ")[1]}
              </span>
              <span className="text-xl font-bold">
                {formatDate(item.lastModifiedOn).split(" ")[0]}
              </span>
            </div>
            <div className="flex-grow pl-6">
              <p className="text-xl font-bold">Payment</p>
            </div>
            <div className="flex gap-10 text-right">
              <div className="flex gap-6 items-center justify-end">
              {item.status && (
                <Trash2
                  className="w-6 h-6 text-red-600 cursor-pointer"
                  onClick={() => {
                    handleDeleteClick(item);
                  }}
                />
              )}
              </div>
              <div>
                  <p className="text-xl text-gray-500">
                    {user?.email &&
                      (item.paidBy.email === user.email
                        ? "you paid"
                        : `${item.paidBy.firstName} paid`)}{" "}
                    to{" "}
                    {user?.email &&
                      (item.paidTo.email === user.email
                        ? "you"
                        : `${item.paidTo.firstName}`)}
                  </p>

                <p
                  className={`font-semibold text-lg ${
                    item.paidBy.email === user?.email
                      ? "text-red-600"
                      : "text-green-600"
                  }`}
                >
                  ${item.amount.toFixed(2)}
                </p>
              </div>
            </div>
          </div> 
        </div>  
      );
    }
  };

  return (
    <div className="container mx-auto py-12 px-6 sm:px-8 lg:px-20">
      <div className="mb-8 flex justify-between">
        <h1 className="text-4xl font-bold mb-10">Transactions</h1>
        <div className="flex gap-10">
        
        <Link href="/expenses/users">
        <Button variant="outline" size="lg" className="flex items-center">
        Friends
        </Button>
        </Link>
        <Button 
          variant="outline" 
          size="lg" 
          className="flex items-center"
          onClick={() => setShowAddModal(true)}>
          <Plus />
          <div className="px-1 h-3" />
          <span className="font-medium">Add Expense</span>
        </Button>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="outline">
              <span className="font-medium flex gap-2 align-center justify-between">
                {filterStatus === "all"
                  ? "All"
                  : filterStatus === "active"
                    ? "Active"
                    : "Inactive"}
                <ChevronDown className="w-4 h-4" />
              </span>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-40">
            <DropdownMenuRadioGroup
              value={filterStatus}
              onValueChange={setFilterStatus}
            >
              <DropdownMenuRadioItem value="all">All</DropdownMenuRadioItem>
              <DropdownMenuRadioItem value="active">
                Active
              </DropdownMenuRadioItem>
              <DropdownMenuRadioItem value="inactive">
                Inactive
              </DropdownMenuRadioItem>
            </DropdownMenuRadioGroup>
          </DropdownMenuContent>
        </DropdownMenu>
        
      </div>
      </div>
      {groupedExpenses.map(([month, items]) => (
        <div key={month}>
          <h3 className="text-2xl font-semibold mb-2">{month}</h3>
          {items.map(renderTransactionItem)}
        </div>
      ))}


<Dialog open={showAddModal} onOpenChange={setShowAddModal}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle className="text-2xl">Add Expense</DialogTitle>
            <DialogDescription className="py-4">Add the details of the Expense.</DialogDescription>
          </DialogHeader>
          <form onSubmit={handleAddSubmit} className="space-y-4">
            <div>
              <Label htmlFor="description">Description</Label>
              <Textarea
                id="description"
                value={description}
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
      </Dialog>
    
      
    </div>
  );
};

export default ExpenseComponent;

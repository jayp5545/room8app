"use client";
import React, { useEffect, useState, useCallback } from "react";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { RefreshCcw } from "lucide-react";
import { useAuth } from "@/context/AuthContext";
import { Button } from "@/components/ui/button";
import config from "@/config";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import axios from "axios";
import { toast } from "../ui/use-toast";

interface UserExpense {
  from: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
  };
  to: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
  };
  amount: number;
  status: boolean;
}

type FriendsMap = {
  [key: string]: number;
};

const UserExpenseComponent = () => {
  const { token, user } = useAuth();
  const [userExpenses, setUserExpenses] = useState<UserExpense[]>([]);

  const fetchUserExpensesData = useCallback(async () => {
    try {
      const url = `${config.apiBaseUrl}/api/v1/settleup/calculations`;
      const response = await fetch(url, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      const data = await response.json();
      console.log(data);
      setUserExpenses(data);
    } catch (err) {
      console.log(err);
    }
  }, [token]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        await fetchUserExpensesData();
      } catch (error) {
        console.error("Failed to fetch expenses:", error);
      }
    };
    if (token) {
      fetchData();
    }
  }, [token, fetchUserExpensesData]);

  const calculateExpenses = () => {
    const friendsYouOwe: FriendsMap = {};
    const friendsWhoOweYou: FriendsMap = {};

    userExpenses.forEach((expense) => {
      const { from, to, amount, status } = expense;
      if (from.email === user?.email) {
        if (status) {
          friendsYouOwe[to.email] = (friendsYouOwe[to.email] || 0) + amount;
        } else {
          friendsWhoOweYou[to.email] =
            (friendsWhoOweYou[to.email] || 0) + amount;
        }
      } else if (to.email === user?.email) {
        if (status) {
          friendsWhoOweYou[from.email] =
            (friendsWhoOweYou[from.email] || 0) + amount;
        } else {
          friendsYouOwe[from.email] = (friendsYouOwe[from.email] || 0) + amount;
        }
      }
    });

    return { friendsYouOwe, friendsWhoOweYou };
  };

  const handleSettleUp = async (email: string, amount: number) => {
    const BACKEND_SETTLEUP_API = config.apiBaseUrl + "/api/v1/settleup/add";
    const requestBody = {
      to: email,
      amount: amount,
    };

    const res = await axios
      .post(BACKEND_SETTLEUP_API, requestBody, {
        validateStatus: function (status) {
          return status === 200 || status === 400 || status === 201;
        },
      })
      .then((response) => {
        const status_code = response.status;
        console.log(`settleup add response: ${response.data}`);
        if (status_code === 200 || status_code === 201) {
          toast({
            title: "success",
            description: `settled up for ${amount}`,
          });
        }
        if (status_code === 400) {
          toast({
            title: "Something went wrong",
            description: <p>{"Please try again..."}</p>,
          });
        }
      })
      .catch((error) => {
        console.error(`Internal Server Error!!!, error: ${error}`);
        toast({
          title: "Something went wrong",
          description: <p>{"Please try again..."}</p>,
        });
      });
  };

  const settleUpDialogue = (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline" className="h-full">
          Settle Up
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Settle up</DialogTitle>
          <DialogDescription>with friends</DialogDescription>
        </DialogHeader>
        {Object.entries(calculateExpenses().friendsYouOwe).map(
          ([email, amount]) => (
            <div key={email} className="w-full">
              <Card className="flex w-full justify-between items-center p-2">
                <p className="text-base font-bold">{email}</p>
                <Button
                  variant="secondary"
                  onClick={() =>
                    handleSettleUp(email as string, amount as number)
                  }
                >
                  <div className="text-xs flex flex-col justify-between items-center">
                    <p className="">You owe</p>
                    <p className="font-bold text-red-700">
                      ${amount.toFixed(2)}
                    </p>
                  </div>
                </Button>
              </Card>
            </div>
          ),
        )}

        {/* {Object.entries(calculateExpenses().friendsWhoOweYou).map(
          ([email, amount]) => (
            <div key={email} className="w-full">
              <Card className="flex w-full justify-between items-center p-2">
                <p className="text-base font-bold">{email}</p>
                <Button
                  variant="secondary"
                  // onClick={() =>
                  //   handleSettleUp(email as string, amount as number)
                  // }
                >
                  <div className="text-xs flex flex-col justify-between items-center">
                    <p className="">You lent</p>
                    <p className="font-bold text-green-700">
                      ${amount.toFixed(2)}
                    </p>
                  </div>
                </Button>
              </Card>
            </div>
          ),
        )} */}
      </DialogContent>
    </Dialog>
  );

  return (
    <div className="container mx-auto py-12 px-6 sm:px-8 lg:px-20">
      <div className="flex justify-between">
        <h1 className="text-3xl font-bold mb-8">Friends</h1>
        <div className="flex h-10 justify-between items-center mb-4 gap-4">
          {settleUpDialogue}
          <Button
            className="flex items-center w-10 py-2 px-2 rounded border-gray-200 hover:bg-gray-100"
            onClick={fetchUserExpensesData}
          >
            <RefreshCcw className="w-4 h-4 " />
          </Button>
        </div>
      </div>
      <div className="flex flex-wrap -mx-4">
        {Object.entries(calculateExpenses().friendsYouOwe).map(
          ([email, amount]) => (
            <div key={email} className="w-full md:w-1/2 xl:w-1/3 p-4">
              <Card>
                <CardHeader>
                  <p className="text-lg font-bold">{email}</p>
                </CardHeader>
                <CardContent>
                  <p className="text-gray-500">You owe</p>
                  <p className="text-lg font-bold text-red-600">
                    ${amount.toFixed(2)}
                  </p>
                </CardContent>
              </Card>
            </div>
          ),
        )}

        {Object.entries(calculateExpenses().friendsWhoOweYou).map(
          ([email, amount]) => (
            <div key={email} className="w-full md:w-1/2 xl:w-1/3 p-4">
              <Card>
                <CardHeader>
                  <p className="text-lg font-bold">{email}</p>
                </CardHeader>
                <CardContent>
                  <p className="text-gray-500">You lent</p>
                  <p className="text-lg font-bold text-green-600">
                    ${amount.toFixed(2)}
                  </p>
                </CardContent>
              </Card>
            </div>
          ),
        )}
      </div>
    </div>
  );
};

export default UserExpenseComponent;

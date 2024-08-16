"use client";

import React from "react";

import { useState, useEffect } from "react";
import TaskList from "@/components/tasks/tasksList";
import TaskHeader from "@/components/tasks/taskHeader";
import ProtectedRoute from "@/components/protected-route";
import { useAuth } from "@/context/AuthContext";
import { PageHeader } from "@/components/page-header";
import EmptyRoomCard from "@/components/empty-room";
import config from "@/config";
interface Task {
  id: number;
  name: string;
  taskDate: string;
  assignedTO: string;
  description: string;
}

export default function Tasks() {
  const [lists, setLists] = useState([]);

  const { token, user, room } = useAuth();

  async function fetchData() {
    try {
      const response = await fetch(`${config.apiBaseUrl}/task/get/all`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      const data = await response.json();
      setLists(data);
    } catch (error) {
      console.error("Error fetching task lists:", error);
    }
  }

  useEffect(() => {
    fetchData();
  }, [user, token]);

  if (!room) {
    return (
      <div className="flex min-h-[600px] justify-center items-center">
        <EmptyRoomCard />
      </div>
    );
  }

  return (
    <ProtectedRoute>
      <PageHeader />
      <div className="px-20 py-11">
        <TaskHeader />
        <TaskList tasks={lists} fetchData={fetchData} />
      </div>
    </ProtectedRoute>
  );
}

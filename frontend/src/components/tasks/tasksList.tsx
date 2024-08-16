"use client";

import { useAuth } from "@/context/AuthContext";
import TaskComponent from "./taskComponent";
import { TaskType } from "@/types/types";
import EmptyRoomCard from "../empty-room";

interface TaskListProps {
  tasks: TaskType[];
  fetchData: any;
}

export default function TaskList({ tasks, fetchData }: TaskListProps) {
  return (
    <div className="px-20 py-8">
      {tasks?.map &&
        tasks.map((task) => (
          <TaskComponent key={task.id} task={task} fetchData={fetchData} />
        ))}
    </div>
  );
}

 
import React from 'react';
import AddTask from '@/components/tasks/addTask';
 export default function TaskForm() {
    return (
      <div className="flex justify-center w-full py-12">
      <div className="w-[40%] ">
         <AddTask/>
      </div>
      </div>
    )
   }
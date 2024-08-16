"use client";
import React from "react";
import ProtectedRoute from "@/components/protected-route";
import Announcements from "@/components/announcements/announcements";
import { PageHeader } from "@/components/page-header";
import AnnouncementComponent from "@/components/announcements/announcementComponent";
import EmptyRoomCard from "@/components/empty-room";
import { useAuth } from "@/context/AuthContext";


export default function AnnouncementsPage() {
  const { room } = useAuth();

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
      <div className="w-full py-8 px-4 md:px-6">
        <Announcements />
        {/* <AnnouncementComponent/> */}
      </div>
    </ProtectedRoute>
  );
}

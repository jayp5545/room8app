import React from "react";
import ProtectedRoute from "@/components/protected-route";
import { PageHeader } from "@/components/page-header";
import Profile from "@/components/profile/profile";

const ProfilePage = () => {
  return (
    <ProtectedRoute>
      <PageHeader />
      <div className="w-full mx-auto py-8 px-4 md:px-6">
        <Profile />
      </div>
    </ProtectedRoute>
  );
};
export default ProfilePage;

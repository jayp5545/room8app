// @ts-nocheck

"use client";

import RoomCard from "./room-card";
import UserCard from "./user-card";
import { useAuth } from "@/context/AuthContext";
import axios from "axios";
import { toast } from "../ui/use-toast";
import { useEffect, useState } from "react";
import {
  ExpenseType,
  RoomInfoType,
  SettleUpType,
  TransactionType,
  UserType,
} from "@/types/types";
import { Loading } from "../loading/loading";
import { useRouter } from "next/navigation";
import TransactionTable from "./activity-table";
import config from "@/config";

const default_user: UserType = {
  firstName: "-----",
  lastName: "-----",
  email: "thisdoesnt@exist.com",
};

export default function Profile() {
  const { token, setRoom } = useAuth();
  const router = useRouter();

  const [primaryUser, setPrimaryUser] = useState<UserType>(default_user);
  const [users, setUsers] = useState<UserType[]>([]);
  const [roomInfo, setRoomInfo] = useState<RoomInfoType | null>(null);
  const [transactions, setTransactions] = useState<TransactionType[]>([]);
  const [loading, setLoading] = useState(true);

  const handleLeave = async () => {
    const BACKEND_LEAVE_ROOM_API = config.apiBaseUrl + "/api/v1/room/leave";
    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };

    const res = await axios
      .get(BACKEND_LEAVE_ROOM_API, { headers: headers })
      .then((response) => {
        const status_code = response.status;
        if (status_code === 200) {
          toast({
            title: "Left the room",
          });
          router.replace("/");
        }
        if (status_code === 400) {
          toast({
            title: "Couldn't leave the room",
          });
        }
      })
      .catch((error) => {
        console.error(`Internal Server Error!!!, error: ${error}`);
        toast({
          title: "something went wrong!",
        });
      });
  };

  const handleDelete = async () => {
    const BACKEND_DELETE_ROOM_API =
      config.apiBaseUrl + `/api/v1/room/delete/${roomInfo?.id}`;
    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };

    const res = await axios
      .delete(BACKEND_DELETE_ROOM_API, { headers: headers })
      .then((response) => {
        const status_code = response.status;
        if (status_code === 200) {
          toast({
            title: "Deleted the room",
          });
          setRoom(null);
          router.replace("/");
        }
        if (status_code === 400) {
          toast({
            title: "Couldn't delete the room",
          });
        }
      })
      .catch((error) => {
        console.error(`Internal Server Error!!!, error: ${error}`);
        toast({
          title: "something went wrong!",
        });
      });
  };

  const handleUpdateUser = async (updatedUser: UserType) => {
    const BACKEND_UPDATE_USER_API =
      config.apiBaseUrl + "/api/v1/profile/update";
    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };
    const body = {
      email: updatedUser.email,
      firstName: updatedUser.firstName,
      lastName: updatedUser.lastName,
    };

    const res = await axios
      .post(BACKEND_UPDATE_USER_API, body, { headers: headers })
      .then((response) => {
        const status_code = response.status;
        if (status_code === 202) {
          setPrimaryUser(updatedUser);
          toast({
            title: "User information updated successfully",
          });
          fetchProfileData();
        }
        if (status_code === 400) {
          toast({
            title: "Failed to update user information",
            variant: "destructive",
          });
        }
      })
      .catch((error) => {
        console.error(`Internal Server Error!!!, error: ${error}`);
        toast({
          title: "something went wrong!",
        });
      });
  };

  const fetchActivityData = async () => {
    const BACKEND_ACTIVITY_FETCH_API =
      config.apiBaseUrl + "/api/v1/activity/get/all";
    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };

    const res = await axios
      .get(BACKEND_ACTIVITY_FETCH_API, { headers: headers })
      .then((response) => {
        const status_code = response.status;
        if (status_code === 200) {
          const transformedData: TransactionType[] = response.data.map(
            (item: any) => ({
              id: item.id,
              type: item.type,
              date: new Date(item.date),
              details: item.details,
              room: item.room,
              expense: item.expense
                ? ({
                    ...item.expense,
                    dateOfCreation: new Date(item.expense.dateOfCreation),
                    lastModifiedOn: item.expense.lastModifiedOn
                      ? new Date(item.expense.lastModifiedOn)
                      : undefined,
                  } as ExpenseType)
                : undefined,
              settleUp: item.settleUp
                ? ({
                    ...item.settleUp,
                    dateOfCreation: new Date(item.settleUp.dateOfCreation),
                    lastModifiedOn: item.settleUp.lastModifiedOn
                      ? new Date(item.settleUp.lastModifiedOn)
                      : undefined,
                  } as SettleUpType)
                : undefined,
            }),
          );

          setTransactions(transformedData);
        }
        if (status_code === 400) {
          toast({
            title: "Couldn't fetch activity data",
          });
        }
      })
      .catch((error) => {
        console.error(`Internal Server Error!!!, error: ${error}`);
        toast({
          title: "something went wrong!",
        });
      });
  };

  const fetchUsersData = async () => {
    const BACKEND_USERS_FETCH_API =
      config.apiBaseUrl + "/api/v1/room/get/all/users";
    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };

    const res = await axios
      .get(BACKEND_USERS_FETCH_API, {
        headers: headers,
        validateStatus: (status) => status === 200 || status === 400,
      })
      .then((response) => {
        const status = response.status;
        if (status === 200) {
          const users = response.data;
          if (users) {
            setUsers(users);
          }
        }
        if (status === 400) {
          toast({
            title: "Failed to load room members data",
          });
        }
      })
      .catch((error) => {
        console.error(`Internal Server Error!!!, error: ${error}`);
        toast({
          title: "something went wrong!",
        });
      });
  };

  const fetchProfileData = async () => {
    const BACKEND_PROFILE_FETCH_API = config.apiBaseUrl + "/api/v1/profile/get";
    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };

    const res = await axios
      .get(BACKEND_PROFILE_FETCH_API, {
        headers: headers,
        validateStatus: (status) => status === 200 || status === 400,
      })
      .then(async (response) => {
        const status = response.status;
        if (status === 200) {
          const user = response.data?.userDTOResponse;
          const roomInfo = response.data?.roomDTOResponse;
          const isodate = new Date(response.data?.joinDate);
          if (user) {
            setPrimaryUser({
              firstName: user.firstName,
              lastName: user.lastName,
              email: user.email,
            });
          }
          if (roomInfo) {
            setRoomInfo({
              id: roomInfo.id,
              name: roomInfo.name,
              code: roomInfo.code,
              status: Boolean(roomInfo.active),
              members: Number(roomInfo.members),
              joinDate: isodate,
            });
            await fetchUsersData();
            await fetchActivityData();
          }
        }
        if (status === 400) {
          toast({
            title: "Failed to load profile data",
          });
        }
      })
      .catch((error) => {
        console.error(`Internal Server Error!!!, error: ${error}`);
        toast({
          title: "something went wrong!",
        });
      });
    setLoading(false);
  };

  useEffect(() => {
    fetchProfileData();
  }, []);

  if (loading) {
    return (
      <div className="flex w-full h-screen justify-center items-center">
        <Loading />
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4 space-y-8">
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="space-y-8">
          <UserCard
            user={primaryUser}
            className="w-full"
            onUpdateUser={handleUpdateUser}
          />
          <RoomCard
            primary_user={primaryUser}
            users={users}
            room={roomInfo}
            onLeave={handleLeave}
            onDelete={handleDelete}
            className="w-full"
          />
        </div>
        <div className="space-y-4">
          <h1 className="text-2xl font-bold">Expense Activity</h1>
          <TransactionTable transactions={transactions} />
        </div>
      </div>
    </div>
  );
}

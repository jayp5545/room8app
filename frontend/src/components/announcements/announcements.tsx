"use client";

import { useEffect, useMemo, useState } from "react";
import { useAuth } from "@/context/AuthContext";
import { AnnouncementType } from "@/types/types";
import AnnouncementCard from "./announcement-card";
import fetchAnnouncementData from "./fetch-announcements";
import { Loading } from "../loading/loading";
import { AddAnnouncementDialog } from "./dialog/add-announcement-dialog";
import axios from "axios";
import { toast } from "../ui/use-toast";
import { Button } from "../ui/button";
import { BellOffIcon, RefreshCwIcon } from "lucide-react";
import { Card, CardDescription, CardTitle } from "../ui/card";
import config from "@/config";

export default function Announcements() {
  const { token } = useAuth();

  const [announcements, setAnnouncements] = useState<AnnouncementType[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const fetchAnnouncements = async () => {
    setIsLoading(true);
    try {
      const data = await fetchAnnouncementData(token);
      setAnnouncements(data);
    } catch (error) {
      console.error("Error fetching announcements:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleAdd = async (title: string, description: string) => {
    const BACKEND_API = config.apiBaseUrl;
    const ADD_ANNOUNCEMENT_API = BACKEND_API + "/api/v1/announcements/add";

    const headers = {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };

    const body = {
      title: title,
      description: description,
    };

    await axios
      .post(ADD_ANNOUNCEMENT_API, body, {
        headers: headers,
        validateStatus: (status) => {
          return status === 200 || status === 400;
        },
      })
      .then((response) => {
        const status = response.status;
        if (status === 200) {
          toast({
            title: response.data?.title,
            description: "Added successfully",
          });
        }
        if (status === 400) {
          toast({
            title: response.data?.errorMessage,
          });
        }
      })
      .catch((error) => {
        console.error("Error fetching announcements:", error);
        toast({
          title: "Failed to fetch announcements",
          description: "Please try again later",
        });
      });

    fetchAnnouncements();
  };

  useEffect(() => {
    fetchAnnouncements();
  }, []);

  if (isLoading) {
    return (
      <div className="flex w-full h-screen justify-center items-center">
        <Loading />
      </div>
    );
  }

  const NoAnnouncementDisplay = () => {
    return (
      <div className="flex items-center justify-center w-full py-32">
        <Card className="w-full max-w-md p-6 flex flex-col items-center justify-center text-center">
          <BellOffIcon className="w-12 h-12 text-muted-foreground" />
          <CardTitle className="mt-4 text-xl font-semibold">
            No Announcements
          </CardTitle>
          <CardDescription className="mt-2 text-muted-foreground">
            There are currently no announcements for your roommates.
          </CardDescription>
        </Card>
      </div>
    );
  };

  const AnnouncementCards = () => {
    return (
      <div className="grid gap-6">
        {announcements.map((announcement, index) => (
          <div key={index}>
            <AnnouncementCard
              {...announcement}
              refreshAnnouncements={fetchAnnouncements}
            />
          </div>
        ))}
      </div>
    );
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center space-y-2">
        <div>
          <p>Stay up-to-date with the latest news and updates.</p>
        </div>
        <div className="flex justify-center  gap-4">
          <AddAnnouncementDialog onAdd={handleAdd} />
          <Button
            className="w-fit h-fit"
            onClick={fetchAnnouncements}
            disabled={isLoading}
          >
            <RefreshCwIcon />
          </Button>
        </div>
      </div>
      {announcements.length === 0
        ? NoAnnouncementDisplay()
        : AnnouncementCards()}
    </div>
  );
}

import { AnnouncementType } from "@/types/types";
import axios from "axios";
import { toast } from "../ui/use-toast";
import config from "@/config";
interface ServerAnnouncementType {
  id: number;
  title: string;
  description: string;
  userPostedFirstName: string;
  userPostedLastName: string;
  userModifiedFirstName: string | null;
  userModifiedLastName: string | null;
  postedDateTime: string;
  lastUpdatedDateTime: string | null;
}

function convertAnnouncement(
  serverAnnouncement: ServerAnnouncementType,
): AnnouncementType {
  return {
    ...serverAnnouncement,
    id: serverAnnouncement.id.toString(), // Convert id to string
    postedDateTime: new Date(serverAnnouncement.postedDateTime),
    lastUpdatedDateTime: serverAnnouncement.lastUpdatedDateTime
      ? new Date(serverAnnouncement.lastUpdatedDateTime)
      : new Date(serverAnnouncement.postedDateTime), // Use postedDateTime if lastUpdatedDateTime is null
  };
}

export default async function fetchAnnouncementData(
  token: string | null,
): Promise<AnnouncementType[]> {
  const BACKEND_API = config.apiBaseUrl;
  const ANNOUNCEMENT_FETCH_API = BACKEND_API + "/api/v1/announcements/get/all";

  const headers = {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  };

  let announcements: Array<AnnouncementType> = [];

  await axios
    .get(ANNOUNCEMENT_FETCH_API, {
      headers: headers,
      validateStatus: (status) => {
        return status === 200 || status === 400;
      },
    })
    .then((response) => {
      const status = response.status;
      if (status === 200) {
        announcements = response.data.map(convertAnnouncement);
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

  return announcements;
}

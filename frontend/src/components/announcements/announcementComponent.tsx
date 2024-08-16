// @ts-nocheck

'use client';
import React, { useEffect } from "react";
import { Button } from "@/components/ui/button"
import { Plus ,Pencil, Trash2, SearchX, CloudFog} from "lucide-react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog"
import { Card,CardHeader,CardTitle,CardDescription,CardContent } from "@/components/ui/card"
import { useAuth } from "@/context/AuthContext";
import { Label } from "@/components/ui/label"
import { Input } from "@/components/ui/input"
import { useState,useCallback } from "react"
import { Textarea } from "@/components/ui/textarea"
import config from "@/config"
interface Announcement {
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

 const AnnouncementComponent = () => {
  const [showAddModal, setShowAddModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false)
  const [title, setTitle] = useState("")
  const [description, setDescription] = useState("");
  const [currentAnnouncement, setCurrentAnnouncement] = useState<Announcement | null>(null);

  const { token } = useAuth();
  const [announcements, setAnnouncements] = useState<Announcement[]>([]);

  const url = config.apiBaseUrl;
  const fetchAnnouncementsData = useCallback(async () => {
    const fecthURL = `${url}/api/v1/announcements/get/all`;
    try {
      const response = await fetch(fecthURL, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      const data = await response.json();
      setAnnouncements(data);
    } catch (err) {
      console.log(err);
    }
  }, [token]);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        await fetchAnnouncementsData();
      } catch (error) {
        console.error("Failed to fetch announcements:", error);
      } finally {
        setLoading(false);
      }
    };
    if (token) {
      fetchData();
    }
  }, [token, fetchAnnouncementsData]);


  const [loading, setLoading] = useState(false);

  const handleAddSubmit = async (e:React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
     const addUrl = `${url}/api/v1/announcements/add`;
      const response = await fetch(addUrl, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ title, description }),
      });
      const newAnnouncement = await response.json();
      setAnnouncements((prev) => [...prev, newAnnouncement]);
      setShowAddModal(false);
    } catch (err) {
      console.log(err);
    }
  }
  
  const handleUpdateSubmit = async (e:React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!currentAnnouncement) return;

    try {
        const updateUrl = `${url}/api/v1/announcements/update/${currentAnnouncement.id}`;
    console.log(updateUrl);
      const response = await fetch(updateUrl, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ title, description }),
      });
      const updatedAnnouncement = await response.json();
      setAnnouncements((prev) => prev.map((ann) => (ann.id === updatedAnnouncement.id ? updatedAnnouncement : ann)));
      setShowUpdateModal(false);
    } catch (err) {
      console.log(err);
    }
  }

  const handleDeleteClick = async (id: number) => {
    try {
      const response = await fetch(`${config.apiBaseUrl}/announcements/delete/${id}`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        setAnnouncements((prev) => prev.filter((ann) => ann.id !== id));
      } else {
        console.log("Error deleting announcement");
      }

    } catch (err) {
      console.log(err);
    }
  };

  const handleEditClick = (announcement: Announcement) => {
    setCurrentAnnouncement(announcement);
    setTitle(announcement.title);
    setDescription(announcement.description);
    setShowUpdateModal(true);
  };

  


  return (
    
    <div className="container mx-auto py-12 px-6 sm:px-8 lg:px-10">
      <div className="mb-8 flex justify-between">
        <div>
          <h1 className="text-4xl font-bold">Announcements</h1>
          <p className="text-muted-foreground">Latest Announcements for Room</p>
        </div>
        <Button 
          variant="outline" 
          size="lg" 
          className="flex items-center"
          onClick={() => setShowAddModal(true)}>
          <Plus />
          <div className="px-1 h-3" />
          <span className="text-lg">Add Announcement</span>
        </Button>
      </div>

      {announcements.length >0 ? (announcements.map((announcement) => (
        <Card key={announcement.title} className="my-8">
          <CardHeader className="flex justify-between items-start">
            <div className="w-full">
              <div className="flex justify-between">
                <CardTitle className="text-3xl">{announcement.title}</CardTitle>
                <div className="flex gap-6 h-100">
                  <Pencil className="cursor-pointer" onClick={() => handleEditClick(announcement)} />
                  <Trash2 className="cursor-pointer" onClick={() => handleDeleteClick(announcement.id)} />
                </div>
             
              </div>
              <CardDescription className=" py-5 text-xl">
                {announcement.description}
              </CardDescription>
            </div>
         
          </CardHeader>

          <CardContent>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <p className="text-lg  font-bold">Posted by</p>
                <p className="   ">{announcement.userPostedFirstName} {announcement.userPostedLastName}</p>
              </div>
              <div>
                <p className="text-lg  font-bold">Last modified by</p>
                <p className=" ">{announcement.userModifiedFirstName} {announcement.userModifiedLastName}</p>
              </div>
              <div>
                <p className="text-lg  font-bold">Created</p>
                <p className="text-gray-600">
                {new Date(announcement.postedDateTime).toLocaleDateString('en-US', {
                  year: 'numeric',
                  month: '2-digit',
                  day: '2-digit'
                })} 
                {' '} 
                {new Date(announcement.postedDateTime).toLocaleTimeString('en-US', {
                  hour: '2-digit',
                  minute: '2-digit',
                  hour12: true
                })}
                </p>
              </div>
              <div>
                <p className="text-lg font-bold">Last modified</p>
                <p className="text-gray-600">
                {announcement.lastUpdatedDateTime  ? 
                new Date(announcement.lastUpdatedDateTime).toLocaleDateString('en-US', {
                  year: 'numeric',
                  month: '2-digit',
                  day: '2-digit'
                }):     '-'
                } 
                {' '} 
                {announcement.lastUpdatedDateTime  ?  
                new Date(announcement.lastUpdatedDateTime).toLocaleTimeString('en-US', {
                  hour: '2-digit',
                  minute: '2-digit',
                  hour12: true
                }) : ''
              }
                </p>
              </div>
            </div>
          </CardContent>
          
        </Card>
      ))) : (
        <div className="flex flex-col items-center justify-center min-h-[50vh] gap-6">
          <SearchX className="h-12 w-12 text-muted-foreground" />
          <h3 className="font-bold text-3xl tracking-tight">No Announcements Yet</h3>
          <p className="text-muted-foreground text-center max-w-md text-lg">
            You can start adding announcements by clicking the &quot;Add Announcement&quot; button  
          </p>
      
        </div>
      )}


<Dialog open={showAddModal} onOpenChange={setShowAddModal}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle className="text-2xl">Add Announcement</DialogTitle>
            <DialogDescription className="py-4">Add the details of the announcement.</DialogDescription>
          </DialogHeader>
          <form onSubmit={handleAddSubmit} className="space-y-4">
            <div>
              <Label htmlFor="title">Title</Label>
              <Input id="title" value={title} onChange={(e) => setTitle(e.target.value)} required />
            </div>
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
            <DialogFooter>
              <Button type="submit">
                Add Announcement
              </Button>
      
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>

      <Dialog open={showUpdateModal} onOpenChange={setShowUpdateModal}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle className="text-2xl">Edit Announcement</DialogTitle>
            <DialogDescription className="py-4">Update the details for your announcement.</DialogDescription>
          </DialogHeader>
          <form onSubmit={handleUpdateSubmit} className="space-y-4">
            <div>
              <Label htmlFor="title">Title</Label>
              <Input id="title" value={title} onChange={(e) => setTitle(e.target.value)} required />
            </div>
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
            <DialogFooter>
              <Button type="submit">
                Save Announcement
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
      
    </div>
    
  );
};

export default AnnouncementComponent;



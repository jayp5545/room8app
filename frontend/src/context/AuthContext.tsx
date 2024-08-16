"use client";

import { useRouter } from "next/navigation";
import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";

type Room = {
  id: number;
  name: string;
  members: number;
  code: number;
  active: boolean;
};

type AuthContextType = {
  user: { email: string } | null;
  token: string | null;
  room: Room | null;
  login: (email: string, token: string, room: Room) => void;
  logout: () => void;
  setRoom: (room: Room | null) => void;
  isLoading: boolean;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

type AuthProviderProps = {
  children: ReactNode;
};

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [user, setUser] = useState<{ email: string } | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [room, setRoom] = useState<Room | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const loadStoredData = () => {
      const savedUser = localStorage.getItem("user");
      const savedToken = localStorage.getItem("token");
      const savedRoom = localStorage.getItem("room");

      if (savedUser) {
        setUser(JSON.parse(savedUser));
      }
      if (savedToken) {
        setToken(savedToken);
      }
      if (savedRoom && savedRoom !== "undefined") {
        setRoom(JSON.parse(savedRoom));
      }
      setIsLoading(false);
    };

    loadStoredData();
  }, []);

  const login = (email: string, token: string, room: Room) => {
    setUser({ email });
    setToken(token);
    setRoom(room);
    localStorage.setItem("user", JSON.stringify({ email }));
    localStorage.setItem("token", token);
    localStorage.setItem("room", JSON.stringify(room));
    router.push("/");
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    setRoom(null);
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    localStorage.removeItem("room");
    router.push("/login");
  };

  const updateRoom = (room: Room | null) => {
    setRoom(room);
    localStorage.setItem("room", JSON.stringify(room));
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        room,
        login,
        logout,
        setRoom: updateRoom,
        isLoading,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

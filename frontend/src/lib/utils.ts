import { UserType } from "@/types/types";
import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function formatDate(date: Date) {
  return date.toLocaleString("en-US", {
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "numeric",
    minute: "numeric",
    hour12: true,
  });
}

export function getInitialsFromUser(user: UserType) {
  return getInitials(user.firstName, user.lastName);
}

export function getInitials(firstName: string, lastName: string) {
  return `${firstName[0]}${lastName[0]}`;
}

export function getFullNameFromUser(user: UserType) {
  return getFullName(user.firstName, user.lastName);
}

export function getFullName(firstName: string, lastName: string) {
  return `${firstName} ${lastName}`;
}

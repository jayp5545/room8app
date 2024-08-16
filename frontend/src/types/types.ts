export interface TaskType {
  id: number;
  name: string;
  taskDate: string;
  assignedTo: any;
  description: string;
}

export type UserInfoType = {
  firstName: String;
  lastName: String;
  email: String;
};

export type RoomInfoType = {
  name: string;
  code: string;
  status: boolean;
  members: number;
  joinDate: Date;
};

export type AnnouncementType = {
  id: string;
  title: string;
  description: string;
  userPostedFirstName: string;
  userPostedLastName: string;
  userModifiedFirstName: string | null;
  userModifiedLastName: string | null;
  postedDateTime: Date;
  lastUpdatedDateTime: Date;
};

export type UserType = {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
};

export type ExpenseType = {
  id?: number;
  room?: RoomInfoType;
  description: string;
  amount: number;
  paidBy: UserType;
  participants: UserType[];
  status: boolean;
  createdBy?: UserType;
  dateOfCreation: Date;
  lastModifiedBy?: UserType;
  lastModifiedOn?: Date;
};

export type SettleUpType = {
  id?: number;
  room?: RoomInfoType;
  paidBy: UserType;
  paidTo: UserType;
  amount: number;
  status: boolean;
  createdBy: UserType;
  dateOfCreation: Date;
  lastModifiedBy?: UserType;
  lastModifiedOn?: Date;
};

export type TransactionType = {
  id?: number;
  type: string;
  expense?: ExpenseType;
  settleUp?: SettleUpType;
  date: Date;
  details: string;
  room: RoomInfoType;
};

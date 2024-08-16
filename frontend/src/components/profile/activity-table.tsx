import React from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { DollarSign, UserMinus } from "lucide-react";
import { TransactionType } from "@/types/types";
import { ScrollArea } from "../ui/scroll-area";

interface TransactionTableProps {
  transactions: TransactionType[];
}

const TransactionTable: React.FC<TransactionTableProps> = ({
  transactions,
}) => {
  const sortedTransactions = [...transactions].sort(
    (a, b) => new Date(b.date).getTime() - new Date(a.date).getTime(),
  );

  const getUser = (transaction: TransactionType): string => {
    if (transaction.expense) {
      return `${transaction.expense.paidBy.firstName} ${transaction.expense.paidBy.lastName}`;
    } else if (transaction.settleUp) {
      return `${transaction.settleUp.paidBy.firstName} ${transaction.settleUp.paidBy.lastName}`;
    }
    return "Unknown";
  };

  return (
    <ScrollArea className="h-[600px] rounded-md">
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Type</TableHead>
            <TableHead>ID</TableHead>
            <TableHead>User</TableHead>
            <TableHead>Date</TableHead>
            <TableHead>Details</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {sortedTransactions.map((transaction) => (
            <TableRow key={transaction.id}>
              <TableCell>
                {transaction.type.startsWith("Expense") ? (
                  <DollarSign className="h-5 w-5 text-green-500" />
                ) : (
                  <UserMinus className="h-5 w-5 text-blue-500" />
                )}
              </TableCell>
              <TableCell>{transaction.id}</TableCell>
              <TableCell>{getUser(transaction)}</TableCell>
              <TableCell>
                {new Date(transaction.date).toLocaleString("en-US")}
              </TableCell>
              <TableCell>{transaction.details}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </ScrollArea>
  );
};

export default TransactionTable;

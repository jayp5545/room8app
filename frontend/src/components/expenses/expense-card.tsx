import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
} from "@/components/ui/card";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Separator } from "@/components/ui/separator";
import { ReceiptIcon } from "lucide-react";

export default function ExpenseCard() {
  return (
    <Card className="w-full max-w-xl">
      <CardHeader>
        <CardTitle>Milk</CardTitle>
      </CardHeader>
      <CardContent className="flex flex-col gap-4">
        <div className="grid grid-cols-[1fr_auto] items-center gap-4">
          <div className="flex items-center gap-4">
            <div className="bg-primary rounded-full p-2 flex items-center justify-center">
              <ReceiptIcon className="w-6 h-6 text-primary-foreground" />
            </div>
            <div>
              <div className="font-medium">Expense</div>
            </div>
          </div>
          <div className="font-medium text-right">$1,250.00</div>
        </div>
        <div className="flex items-center justify-between gap-4">
          <div>
            <div className="font-medium">Paid By</div>
          </div>
          <div>
            <div className="flex items-center gap-2">
              <Avatar className="border">
                <AvatarFallback>CN</AvatarFallback>
              </Avatar>
              <div>
                <h1 className="font-medium">Catherine Nguyen</h1>
                <p className="text-muted-foreground text-sm">
                  catherine@company.com
                </p>
              </div>
            </div>
          </div>
        </div>
        <div className="grid grid-cols-[repeat(3,auto)] items-center gap-4">
          <div className="flex items-center gap-2">
            <Avatar className="border">
              <AvatarFallback>JP</AvatarFallback>
            </Avatar>
            <div>
              <div className="font-medium">Jared Palmer</div>
              <div className="text-muted-foreground text-sm">
                jared@company.com
              </div>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Avatar className="border">
              <AvatarFallback>CN</AvatarFallback>
            </Avatar>
            <div>
              <div className="font-medium">Catherine Nguyen</div>
              <div className="text-muted-foreground text-sm">
                catherine@company.com
              </div>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Avatar className="border">
              <AvatarFallback>ML</AvatarFallback>
            </Avatar>
            <div>
              <div className="font-medium">Max Leiter</div>
              <div className="text-muted-foreground text-sm">
                max@company.com
              </div>
            </div>
          </div>
        </div>
        <div className="flex items-center gap-4">
          <div className="bg-green-500 rounded-full px-3 py-1 text-green-50 font-medium text-sm">
            Approved
          </div>
          <div className="text-muted-foreground text-sm">
            Created on July 15, 2023
          </div>
          <div className="text-muted-foreground text-sm">
            Last modified on July 18, 2023
          </div>
        </div>
        <div className="flex items-center gap-4 text-sm text-muted-foreground">
          <div>Created by Catherine Nguyen</div>
          <Separator orientation="vertical" />
          <div>Last modified by Max Leiter</div>
        </div>
      </CardContent>
    </Card>
  );
}

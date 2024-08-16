import type { Metadata } from "next";
import { primary_font } from "@/components/fonts";
import "@/styles/globals.css";
import { ThemeProvider } from "@/components/theme-provider";
import NavBar from "@/components/nav/main-nav";
import { Toaster } from "@/components/ui/toaster";
import { AuthProvider } from "@/context/AuthContext";

export const metadata: Metadata = {
  title: "roomM8",
  description: "application to make shared living simpler",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${primary_font.className} antialiased`}>
        <ThemeProvider
          attribute="class"
          defaultTheme="system"
          enableSystem
          disableTransitionOnChange
        >
          <AuthProvider>
            <NavBar />
            {children}
            <Toaster />
          </AuthProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}

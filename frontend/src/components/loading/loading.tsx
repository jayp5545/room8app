import { LoadingSpinner } from "./loading-spinner";

export const Loading = () => {
  return (
    <div className="flex flex-col items-center justify-center h-screen gap-4">
      <LoadingSpinner />
      <div>Gathering the gang...</div>
    </div>
  );
};

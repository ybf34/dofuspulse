import {QueryClient} from "@tanstack/react-query";
import {isErrorResponse} from "@ts-rest/core";
import {initTsrReactQuery} from "@ts-rest/react-query/v5";
import {toast} from "sonner";
import {contract} from "@/services/api/api.contract";
import type {ProblemDetail} from "@/services/api/api.types";

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
    },
  },
});

export const tsr = initTsrReactQuery(contract, {
  baseUrl: import.meta.env.VITE_API_BASE_URL,
  baseHeaders: {
    accept: "application/json",
    "content-type": "application/json",
  },
  credentials: "include",
});

export function getApiUrl(path: `/${string}`) {
  return import.meta.env.VITE_API_BASE_URL + path;
}

export function handleApiError(error: unknown) {
  const fallbackMessage = "An unexpected error occurred";

  if (isErrorResponse(error)) {
    const body = error.body as ProblemDetail;
    if (body["validation-errors"] && body["validation-errors"].length > 0) {
      const validationErrors = body["validation-errors"];
      validationErrors.forEach((validationError) => {
        const message = `${validationError.reason}`;
        toast.error(message, {duration: 5000});
      });
      return;
    }
    const message = body.detail ?? body.title ?? fallbackMessage;
    toast.error(message, {duration: 3000});
  } else {
    toast.error(fallbackMessage, {duration: 3000});
  }
}

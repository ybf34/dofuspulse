import Axios, {type AxiosError, type AxiosRequestConfig} from "axios";
import {env} from "@/lib/env";

type ParamValue =
    | string
    | number
    | boolean
    | string[]
    | Record<string, unknown>;

const customParamsSerializer = (p: Record<string, ParamValue>) => {
  const acc: Record<string, ParamValue> = {};

  for (const [k, v] of Object.entries(p)) {
    if (v && typeof v === "object" && !Array.isArray(v)) {
      Object.assign(acc, v);
    } else {
      acc[k] = v;
    }
  }

  return Object.entries(acc)
  .map(
      ([k, v]) =>
          `${encodeURIComponent(k)}=${
              Array.isArray(v) ? v.join(",") : encodeURIComponent(String(v))
          }`,
  )
  .join("&");
};

export const AXIOS_INSTANCE = Axios.create({
  baseURL: env.VITE_API_BASE_URL,
  withCredentials: true,
  paramsSerializer: customParamsSerializer,
});

type CancellablePromise<T> = Promise<T> & { cancel: () => void };

export const customInstance = <T>(
    config: AxiosRequestConfig,
    options?: AxiosRequestConfig,
): CancellablePromise<T> => {
  const source = Axios.CancelToken.source();

  const promise = AXIOS_INSTANCE({
    ...config,
    ...options,
    cancelToken: source.token,
  }).then(({data}) => data) as CancellablePromise<T>;

  promise.cancel = () => {
    source.cancel("Query was cancelled");
  };

  return promise;
};

export type ErrorType<Error> = AxiosError<Error>;
export type BodyType<BodyData> = BodyData;

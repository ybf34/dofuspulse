import {config} from "dotenv";
import {defineConfig} from "orval";

config();
export default defineConfig({
  api: {
    output: {
      mode: "single",
      target: "src/api/api.ts",
      schemas: "src/api/model",
      client: "react-query",
      mock: false,
      override: {
        mutator: {
          path: "./src/api/axios.ts",
          name: "customInstance",
        },
        query: {
          shouldSplitQueryKey: true,
          mutationOptions: {
            path: "./src/api/custom-mutator-options.ts",
            name: "useCustomMutatorOptions",
          },
        },
      },
    },
    input: {
      target: `${process.env.VITE_API_BASE_URL}/v3/api-docs`,
    },
  },
});

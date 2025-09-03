import {type UseMutationOptions, useQueryClient} from "@tanstack/react-query";
import {
  getGetGearSetByIdQueryKey,
  getGetUserGearSetsQueryKey,
} from "@/api/api.ts";

type MyMutationOptions<
    TData = unknown,
    TError = unknown,
    TVariables = unknown,
    TContext = unknown,
> = UseMutationOptions<TData, TError, TVariables, TContext> &
    Required<
        Pick<UseMutationOptions<TData, TError, TVariables, TContext>, "mutationFn">
    >;

export const useCustomMutatorOptions = <
    TData = unknown,
    TError = unknown,
    TVariables = unknown,
    TContext = unknown,
>(
    options: MyMutationOptions<TData, TError, TVariables, TContext>,
): MyMutationOptions<TData, TError, TVariables, TContext> => {
  const queryClient = useQueryClient();
  const originalOnSuccess = options.onSuccess;

  options.onSuccess = (data, variables, context) => {
    const mutationKey = options.mutationKey?.[0];

    if (mutationKey === "createGearSet" || mutationKey === "deleteGearSet") {
      queryClient.invalidateQueries({queryKey: getGetUserGearSetsQueryKey()});
    }
    if (mutationKey === "unequipItem" || mutationKey === "equipItem") {
      const {gearSetId} = variables as { gearSetId: number };
      queryClient.invalidateQueries({queryKey: getGetUserGearSetsQueryKey()});
      queryClient.invalidateQueries({
        queryKey: getGetGearSetByIdQueryKey(gearSetId),
      });
    }

    if (originalOnSuccess) {
      originalOnSuccess(data, variables, context);
    }
  };

  return options;
};

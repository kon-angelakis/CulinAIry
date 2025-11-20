//Cus hook that saves some api call results to the cache so its cheaper and faster
import { useQuery } from "@tanstack/react-query";
import authAxios from "../config/authAxiosConfig";

export default function useCache({ queryKey, endpoint, formData, method }) {
  return useQuery({
    queryKey: [queryKey, endpoint, formData],
    keepPreviousData: true,
    queryFn: async () => {
      try {
        const response = await authAxios({
          url: endpoint,
          method,
          data: formData,
        });

        return response.data;
      } catch (err) {
        console.error(err);
        return {
          success: false,
          message: "Something went wrong",
          data: [],
        };
      }
    },

    enabled: !!formData,
    staleTime: 1000 * 60 * 5,
    cacheTime: 1000 * 60 * 10,
  });
}

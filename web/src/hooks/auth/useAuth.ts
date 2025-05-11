import { useAuthStore } from '../../store/useAuthStore';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MemberAPI } from '../../libs/api/memberAPI';
import { AuthAPI, type LoginRequest } from '../../libs/api/authAPI';
import { useNavigate } from 'react-router';

const MY_INFO_QUERY_KEY = 'GetMyInfo';

export const useMyInfoQuery = () => {
  const { setAuth, clearAuth } = useAuthStore();

  return useQuery({
    queryKey: [MY_INFO_QUERY_KEY],
    queryFn: async () => {
      const res = await MemberAPI.getMyInfo();
      if (res.result === 'SUCCESS') {
        setAuth(res.data);
        return res.data;
      } else {
        clearAuth();
      }
    },
    retry: false,
    refetchOnWindowFocus: false,
  });
};

export const useLogin = () => {
  const { setIsLoading } = useAuthStore();
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: async (request: LoginRequest) => {
      setIsLoading(true);
      await AuthAPI.login(request);
    },
    onSuccess: async () => {
      await queryClient.refetchQueries({ queryKey: [MY_INFO_QUERY_KEY] });
      navigate('/');
    },
    onSettled: () => {
      setIsLoading(false);
    },
  });
};

export const useLogout = () => {
  const queryClient = useQueryClient();
  const { setIsLoading, clearAuth } = useAuthStore();

  return useMutation({
    mutationFn: async () => {
      setIsLoading(true);
      await AuthAPI.logout();
    },
    onSuccess: () => {
      clearAuth();
      queryClient.clear();
    },
    onSettled: () => {
      setIsLoading(false);
    },
  });
};

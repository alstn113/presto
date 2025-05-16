import { useAuthStore } from '../../store/useAuthStore';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MemberApi } from '../../libs/api/memberApi';
import { AuthApi, type LoginRequest } from '../../libs/api/authApi';
import { useNavigate } from 'react-router';

const MY_INFO_QUERY_KEY = 'GetMyInfo';

export const useMyInfoQuery = () => {
  const { setAuth, clearAuth } = useAuthStore();

  return useQuery({
    queryKey: [MY_INFO_QUERY_KEY],
    queryFn: async () => {
      try {
        const memberInfo = await MemberApi.getMyInfo();
        setAuth(memberInfo);
        return memberInfo;
      } catch (error) {
        console.error('Error fetching member info:', error);
        clearAuth();
      }
    },
  });
};

export const useLogin = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: async (request: LoginRequest) => {
      await AuthApi.login(request);
    },
    onSuccess: async () => {
      await queryClient.refetchQueries({ queryKey: [MY_INFO_QUERY_KEY] });
      navigate('/');
    },
  });
};

export const useLogout = () => {
  const queryClient = useQueryClient();
  const { clearAuth } = useAuthStore();

  return useMutation({
    mutationFn: async () => {
      await AuthApi.logout();
    },
    onSuccess: () => {
      clearAuth();
      queryClient.clear();
    },
  });
};

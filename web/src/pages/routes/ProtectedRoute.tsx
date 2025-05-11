import { Navigate, Outlet } from 'react-router';
import { useAuthStore } from '../../store/useAuthStore';

const ProtectedRoute = () => {
  const { isLoading, isLoggedIn } = useAuthStore();

  if (isLoading) {
    return <div>로딩 중...</div>;
  }

  if (!isLoggedIn) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;

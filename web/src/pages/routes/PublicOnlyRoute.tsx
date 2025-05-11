import { Navigate, Outlet } from 'react-router';
import { useAuthStore } from '../../store/useAuthStore';

const PublicOnlyRoute = () => {
  const { isLoading, isLoggedIn } = useAuthStore();

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-screen">
        로딩 중...
      </div>
    );
  }

  if (isLoggedIn) {
    return <Navigate to="/" />;
  }

  return <Outlet />;
};

export default PublicOnlyRoute;

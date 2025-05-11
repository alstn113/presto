import { Navigate, Outlet } from 'react-router';
import { useAuthStore } from '../../store/useAuthStore';

const PublicOnlyRoute = () => {
  const { isLoggedIn } = useAuthStore();

  if (isLoggedIn) {
    return <Navigate to="/" />;
  }

  return <Outlet />;
};

export default PublicOnlyRoute;

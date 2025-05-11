import { Navigate, Outlet } from 'react-router';
import { useAuthStore } from '../../store/useAuthStore';

const ProtectedRoute = () => {
  const { isLoggedIn } = useAuthStore();

  if (!isLoggedIn) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;

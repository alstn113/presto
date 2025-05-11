import { createBrowserRouter } from 'react-router';
import HomePage from '../HomePage';
import DashboardPage from '../DashboardPage';
import LoginPage from '../LoginPage';
import PublicOnlyRoute from './PublicOnlyRoute';
import ProtectedRoute from './ProtectedRoute';
import NotFoundPage from '../NotFoundPage';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <HomePage />,
  },
  {
    element: <PublicOnlyRoute />,
    children: [
      {
        path: '/login',
        element: <LoginPage />,
      },
    ],
  },
  {
    element: <ProtectedRoute />,
    children: [
      {
        path: '/dashboard',
        element: <DashboardPage />,
      },
    ],
  },
  {
    path: '*',
    element: <NotFoundPage />,
  },
]);

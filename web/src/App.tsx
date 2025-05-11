import { RouterProvider } from 'react-router';
import { useMyInfoQuery } from './hooks/auth/useAuth';
import { router } from './pages/routes/router';

function App() {
  const { isLoading } = useMyInfoQuery();

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return <RouterProvider router={router} />;
}

export default App;

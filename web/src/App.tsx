import { RouterProvider } from 'react-router';
import { useMyInfoQuery } from './hooks/auth/useAuth';
import { router } from './pages/routes/router';

function App() {
  useMyInfoQuery();

  return <RouterProvider router={router} />;
}

export default App;

import { RouterProvider } from 'react-router';
import { useMyInfoQuery } from './hooks/auth/useAuth';
import { router } from './pages/routes/router';

function App() {
  const { isLoading } = useMyInfoQuery();

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-screen">
        로딩 중...
      </div>
    );
  }

  return <RouterProvider router={router} />;
}

export default App;

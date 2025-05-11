import { Link } from 'react-router';
import { useAuthStore } from '../store/useAuthStore';
import { useLogout } from '../hooks/auth/useAuth';

const Navbar = () => {
  const { isLoggedIn, user } = useAuthStore();
  const logout = useLogout();

  const handleLogout = () => {
    logout.mutate();
  };

  return (
    <nav className="bg-gray-800 text-white shadow-md">
      <div className="container mx-auto px-4 py-3 flex justify-between items-center">
        <Link to="/" className="text-2xl font-semibold hover:text-gray-300">
          My App
        </Link>

        <div className="flex items-center space-x-6">
          <Link to="/" className="hover:text-gray-300">
            홈
          </Link>
          <Link to="/dashboard" className="hover:text-gray-300">
            대시보드
          </Link>

          {isLoggedIn && user ? (
            <>
              <span className="text-sm">
                안녕하세요, <strong>{user.username}</strong>님
              </span>
              <button
                onClick={handleLogout}
                disabled={logout.isPending}
                className="px-4 py-2 bg-red-600 rounded hover:bg-red-700 disabled:bg-red-400 text-sm"
              >
                {logout.isPending ? '로그아웃 중...' : '로그아웃'}
              </button>
            </>
          ) : (
            <Link
              to="/login"
              className="px-4 py-2 bg-blue-600 rounded hover:bg-blue-700 text-sm"
            >
              로그인
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;

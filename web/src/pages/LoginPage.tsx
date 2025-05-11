import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router';
import Navbar from '../components/Navbar';
import { useLogin } from '../hooks/auth/useAuth';

const LoginPage = () => {
  const navigate = useNavigate();
  const login = useLogin();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = (e: FormEvent) => {
    e.preventDefault();
    login.mutate(
      { username, password },
      {
        onSuccess: () => {
          navigate('/');
        },
        onError: (error) => {
          console.error('Login failed:', error);
        },
      }
    );
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <div className="flex justify-center items-center py-12 px-4 sm:px-6 lg:px-8">
        <div className="w-full max-w-md space-y-8 bg-white p-8 shadow-xl rounded-2xl">
          <h2 className="text-center text-3xl font-extrabold text-gray-900">
            로그인
          </h2>
          <form onSubmit={handleLogin} className="mt-8 space-y-6">
            <div>
              <label
                htmlFor="username"
                className="block text-sm font-medium text-gray-700"
              >
                사용자 이름
              </label>
              <input
                type="text"
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
                className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-700"
              >
                비밀번호
              </label>
              <input
                type="password"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            <div>
              <button
                type="submit"
                disabled={login.isPending}
                className={`w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white ${
                  login.isPending
                    ? 'bg-blue-400 cursor-not-allowed'
                    : 'bg-blue-600 hover:bg-blue-700'
                } focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500`}
              >
                {login.isPending ? '로그인 중...' : '로그인'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;

import axios from 'axios';
import { BASE_URL } from '../../constants';

export const apiV1Client = axios.create({
  baseURL: `${BASE_URL}/api/v1`,
  withCredentials: true,
});

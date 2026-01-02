import axios from 'axios';

const api = axios.create({
  baseURL: '/api'
});

const getToken = () => localStorage.getItem('nascctv_token');

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const login = async (username, password) => {
  const response = await api.post('/auth/login', { username, password });
  localStorage.setItem('nascctv_token', response.data.accessToken);
  return response.data;
};

export const fetchCameras = async () => {
  try {
    const response = await api.get('/cameras');
    return response.data;
  } catch (error) {
    return [
      {
        id: 1,
        name: '园区南门高清枪机',
        protocol: 'ONVIF',
        streamUrl: 'rtsp://10.0.0.12/stream1',
        status: 'online',
        location: '南门出入口'
      },
      {
        id: 2,
        name: '停车场球机',
        protocol: 'RTSP',
        streamUrl: 'rtsp://10.0.0.21/stream1',
        status: 'offline',
        location: '地下停车场'
      },
      {
        id: 3,
        name: '仓库全景',
        protocol: 'GB/T 28181',
        streamUrl: 'sip:34020000001320000001@10.0.0.30',
        status: 'online',
        location: '仓储区'
      }
    ];
  }
};

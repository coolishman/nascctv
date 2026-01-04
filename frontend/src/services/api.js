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
  const response = await api.get('/cameras');
  return response.data;
};

export const fetchRecordings = async (cameraList = []) => {
  if (!cameraList.length) {
    return [];
  }

  const response = await api.get('/recordings', { params: { cameraId: cameraList[0].id } });
  return response.data.map((recording) => ({
    id: recording.id,
    cameraName: cameraList.find((camera) => camera.id === recording.cameraId)?.name || '未知摄像头',
    window: `${new Date(recording.startedAt).toLocaleString()} - ${new Date(recording.endedAt).toLocaleString()}`,
    format: recording.format,
    size: `${(recording.sizeBytes / 1024 / 1024).toFixed(1)} MB`,
    retentionDays: recording.retentionDays
  }));
};

export const fetchCameraStreams = async (cameraId) => {
  const response = await api.get(`/cameras/${cameraId}/streams`);
  return response.data;
};

export const fetchVideoWall = async (wallId) => {
  const response = await api.get(`/walls/${wallId}`);
  return response.data;
};

export const fetchSettings = async () => {
  const response = await api.get('/settings');
  return response.data;
};

export const updateSettings = async (payload) => {
  const response = await api.put('/settings', payload);
  return response.data;
};

export const createCamera = async (payload) => {
  const response = await api.post('/cameras', payload);
  return response.data;
};

export const updateCamera = async (id, payload) => {
  const response = await api.put(`/cameras/${id}`, payload);
  return response.data;
};

export const deleteCamera = async (id) => {
  const response = await api.delete(`/cameras/${id}`);
  return response.data;
};

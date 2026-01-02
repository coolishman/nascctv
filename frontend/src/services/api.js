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
        vendor: 'Hikvision',
        model: 'DS-2CD2T',
        authType: 'DIGEST',
        streamUrl: 'rtsp://10.0.0.12/stream1',
        status: 'online',
        location: '南门出入口'
      },
      {
        id: 2,
        name: '停车场球机',
        protocol: 'RTSP',
        vendor: 'Dahua',
        model: 'DH-SD',
        authType: 'BASIC',
        streamUrl: 'rtsp://10.0.0.21/stream1',
        status: 'offline',
        location: '地下停车场'
      },
      {
        id: 3,
        name: '仓库全景',
        protocol: 'GB/T 28181',
        vendor: '通用设备',
        model: 'GB28181',
        authType: 'TOKEN',
        streamUrl: 'sip:34020000001320000001@10.0.0.30',
        status: 'online',
        location: '仓储区'
      }
    ];
  }
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

export const fetchVideoWall = async (wallId) => {
  const response = await api.get(`/walls/${wallId}`);
  return response.data;
};

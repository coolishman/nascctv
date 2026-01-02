CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(32) NOT NULL,
  last_login_ip VARCHAR(64),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cameras (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  protocol VARCHAR(64) NOT NULL,
  vendor VARCHAR(64),
  model VARCHAR(64),
  auth_type VARCHAR(32) NOT NULL DEFAULT 'BASIC',
  stream_url VARCHAR(255) NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'offline',
  location VARCHAR(128),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS camera_streams (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  camera_id BIGINT NOT NULL,
  name VARCHAR(128) NOT NULL,
  stream_type VARCHAR(32) NOT NULL,
  stream_url VARCHAR(255) NOT NULL,
  codec VARCHAR(32),
  bitrate_kbps INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_camera_streams_camera FOREIGN KEY (camera_id) REFERENCES cameras(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recordings (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  camera_id BIGINT NOT NULL,
  storage_path VARCHAR(255) NOT NULL,
  format VARCHAR(32) NOT NULL,
  size_bytes BIGINT NOT NULL,
  started_at TIMESTAMP NOT NULL,
  ended_at TIMESTAMP NOT NULL,
  retention_days INT NOT NULL DEFAULT 30,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_recordings_camera FOREIGN KEY (camera_id) REFERENCES cameras(id) ON DELETE CASCADE
);

DROP INDEX IF EXISTS idx_cameras_status ON cameras;
CREATE INDEX idx_cameras_status ON cameras(status);
DROP INDEX IF EXISTS idx_camera_streams_camera_id ON camera_streams;
CREATE INDEX idx_camera_streams_camera_id ON camera_streams(camera_id);
DROP INDEX IF EXISTS idx_recordings_camera_id_started_at ON recordings;
CREATE INDEX idx_recordings_camera_id_started_at ON recordings(camera_id, started_at);

CREATE TABLE IF NOT EXISTS user_camera_permissions (
  user_id BIGINT NOT NULL,
  camera_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, camera_id),
  CONSTRAINT fk_user_camera_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_user_camera_camera FOREIGN KEY (camera_id) REFERENCES cameras(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS video_walls (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  description VARCHAR(255),
  created_by BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_video_walls_user FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS wall_tiles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  wall_id BIGINT NOT NULL,
  camera_id BIGINT,
  position INT NOT NULL,
  row_span INT NOT NULL DEFAULT 1,
  col_span INT NOT NULL DEFAULT 1,
  rotation_seconds INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_wall_tiles_wall FOREIGN KEY (wall_id) REFERENCES video_walls(id) ON DELETE CASCADE,
  CONSTRAINT fk_wall_tiles_camera FOREIGN KEY (camera_id) REFERENCES cameras(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS wall_tile_channels (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tile_id BIGINT NOT NULL,
  camera_id BIGINT NOT NULL,
  order_index INT NOT NULL,
  CONSTRAINT fk_wall_tile_channels_tile FOREIGN KEY (tile_id) REFERENCES wall_tiles(id) ON DELETE CASCADE,
  CONSTRAINT fk_wall_tile_channels_camera FOREIGN KEY (camera_id) REFERENCES cameras(id) ON DELETE CASCADE
);

DROP INDEX IF EXISTS idx_permissions_user_id ON user_camera_permissions;
CREATE INDEX idx_permissions_user_id ON user_camera_permissions(user_id);
DROP INDEX IF EXISTS idx_wall_tiles_wall_id ON wall_tiles;
CREATE INDEX idx_wall_tiles_wall_id ON wall_tiles(wall_id);
DROP INDEX IF EXISTS idx_wall_tile_channels_tile_id ON wall_tile_channels;
CREATE INDEX idx_wall_tile_channels_tile_id ON wall_tile_channels(tile_id);

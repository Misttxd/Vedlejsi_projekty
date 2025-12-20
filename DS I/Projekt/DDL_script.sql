USE BRU0098
GO

DROP TABLE IF EXISTS xx_playlist_items;
DROP TABLE IF EXISTS xx_playlists;
DROP TABLE IF EXISTS xx_user_achievements;
DROP TABLE IF EXISTS xx_achievements;
DROP TABLE IF EXISTS xx_game_sessions;
DROP TABLE IF EXISTS xx_song_variants;
DROP TABLE IF EXISTS xx_songs;
DROP TABLE IF EXISTS xx_users;
GO

-- 1. Tabulka Uživatelů
CREATE TABLE xx_users (
  user_id INT IDENTITY(1,1) PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role CHAR(1) DEFAULT 'P' CHECK (role IN ('P', 'A')) -- P=Player, A=Admin
);

-- 2. Tabulka Skladeb
CREATE TABLE xx_songs (
  song_id INT IDENTITY(1,1) PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  artist VARCHAR(100) NOT NULL,
  bpm INT,
  duration_seconds INT,
  file_path VARCHAR(255)
);

-- 3. Tabulka Varianty Skladeb
CREATE TABLE xx_song_variants (
  variant_id INT IDENTITY(1,1) PRIMARY KEY,
  song_id INT NOT NULL,
  difficulty_name VARCHAR(20) NOT NULL, -- Např. 'Easy', 'Hard'
  note_count INT NOT NULL,
  max_score INT,
  FOREIGN KEY (song_id) REFERENCES xx_songs(song_id) ON DELETE CASCADE
);

-- 4. Tabulka Historie her
CREATE TABLE xx_game_sessions (
  session_id INT IDENTITY(1,1) PRIMARY KEY,
  user_id INT NOT NULL,
  variant_id INT NOT NULL,
  score INT NOT NULL,
  accuracy INT CHECK (accuracy BETWEEN 0 AND 100),
  max_combo INT,
  is_full_combo BIT DEFAULT 0, -- 0 = FALSE, 1 = TRUE
  played_at DATETIME DEFAULT GETDATE(),
  FOREIGN KEY (user_id) REFERENCES xx_users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (variant_id) REFERENCES xx_song_variants(variant_id) ON DELETE CASCADE
);

-- 5. Tabulka Achievementů
CREATE TABLE xx_achievements (
  achievement_id INT IDENTITY(1,1) PRIMARY KEY,
  title VARCHAR(50) NOT NULL,
  description VARCHAR(MAX),
  required_points INT
);

-- 6. Tabulka Získaných Achievementů (Vazba M:N)
CREATE TABLE xx_user_achievements (
  id INT IDENTITY(1,1) PRIMARY KEY,
  user_id INT NOT NULL,
  achievement_id INT NOT NULL,
  earned_at DATETIME DEFAULT GETDATE(),
  FOREIGN KEY (user_id) REFERENCES xx_users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (achievement_id) REFERENCES xx_achievements(achievement_id) ON DELETE CASCADE,
  UNIQUE (user_id, achievement_id) -- Zabraňuje duplicitám
);

-- 7. Tabulka Playlistů
CREATE TABLE xx_playlists (
  playlist_id INT IDENTITY(1,1) PRIMARY KEY,
  user_id INT NOT NULL,
  name VARCHAR(50) NOT NULL,
  is_public BIT DEFAULT 0, -- 0 = Private, 1 = Public
  FOREIGN KEY (user_id) REFERENCES xx_users(user_id) ON DELETE CASCADE
);

-- 8. Tabulka Položek Playlistu (Vazba M:N)
CREATE TABLE xx_playlist_items (
  playlist_id INT NOT NULL,
  song_id INT NOT NULL,
  FOREIGN KEY (playlist_id) REFERENCES xx_playlists(playlist_id) ON DELETE CASCADE,
  FOREIGN KEY (song_id) REFERENCES xx_songs(song_id) ON DELETE CASCADE,
  UNIQUE (playlist_id, song_id) -- Skladba nemůže být v playlistu 2x
);	
GO
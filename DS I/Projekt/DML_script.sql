-- 1. Vložení uživatelů
INSERT INTO xx_users (username, email, password, role) VALUES 
('Admin', 'admin@seznam.cz', 'supertajneheslo123', 'A'),
('MegaBorecCZ', 'karel@email.cz', 'supertajneheslo890', 'P'),
('Petr Parek', 'pepa@email.cz', 'supertajneheslo6767', 'P');

-- 2. Vložení skladeb
INSERT INTO xx_songs (title, artist, bpm, duration_seconds) VALUES 
('Kills', 'Chief Keef', 200, 441),
('Silicon wings', 'Yung Lean', 136, 225),
('Relocate', 'Juice WRLD', 138, 318);

-- 3. Vložení variant obtížnosti
INSERT INTO xx_song_variants (song_id, difficulty_name, note_count, max_score) VALUES 
(1, 'Easy', 500, 50000),
(1, 'Hard', 1500, 150000),
(1, 'Expert', 3000, 300000), 
(2, 'Easy', 500, 50000),
(2, 'Hard', 1500, 150000),
(2, 'Expert', 3000, 300000), 
(3, 'Easy', 500, 50000),
(3, 'Hard', 1500, 150000),
(3, 'Expert', 3000, 300000);

-- 4. Vložení definice achievementů
INSERT INTO xx_achievements (title, description, required_points) VALUES 
('První zářez', 'Odehraj svou první hru', 0),
('Combo borec', 'Dosáhni komba 500 a více', 500),
('Odstřelovač', 'Dosáhni přesnosti 100%', 0);

-- 5. Simulace odehrané hry 
-- is_full_combo = BIT -> 0 = FALSE
INSERT INTO xx_game_sessions (user_id, variant_id, score, accuracy, max_combo, is_full_combo) VALUES 
(2, 2, 145000, 98, 450, 0);

-- 6. Hráč ProGamer123 získal achievement 'First Blood'
INSERT INTO xx_user_achievements (user_id, achievement_id) VALUES 
(2, 1);

-- 7. Hráč si vytvoří playlist "Moje oblíbené"
-- is_public = BIT -> 1 = TRUE
INSERT INTO xx_playlists (user_id, name, is_public) VALUES 
(2, 'UMTTNCJB', 1);

-- 8. Přidání skladby do playlistu
INSERT INTO xx_playlist_items (playlist_id, song_id) VALUES 
(1, 1), -- Kills
(1, 2); -- Silicon wings
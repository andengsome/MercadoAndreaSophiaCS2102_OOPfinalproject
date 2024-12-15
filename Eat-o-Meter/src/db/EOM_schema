CREATE DATABASE IF NOT EXISTS eat-o-meter;

USE eat-o-meter;

CREATE TABLE IF NOT EXISTS users(
    id INT AUTO_INCREMENT UNIQUE,
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100), 
    sex VARCHAR(10),
    age INT,
    height DOUBLE,
    goal VARCHAR(50),
    starting_weight DOUBLE,
    current_weight DOUBLE,
    goal_weight DOUBLE,
    activeness VARCHAR(50),
    calorie_goal DOUBLE,
);

CREATE TABLE IF NOT EXISTS log_items (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    category VARCHAR(100) NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    calories DOUBLE NOT NULL,
    quantity DOUBLE DEFAULT 1,
    unit VARCHAR(50),
    logged_date DATE not NULL,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS diary_entries (
    entry_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    entry_date DATE NOT NULL,
    calories_consumed DOUBLE,
    calories_burned DOUBLE,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS weight_progress (
    progress_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    goal VARCHAR(100) NOT NULL,
    prev_weight DOUBLE NOT NULL,
    current_weight DOUBLE NOT NULL,
    progress DOUBLE NOT NULL,
    status VARCHAR(50) DEFAULT 'no changes',
    date_updated DATETIME DEFAULT CURRENT_TIMESTAMP,
    goal_weight DOUBLE,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

DELIMITER $$

CREATE TRIGGER before_insert_entries BEFORE INSERT ON diary_entries 
FOR EACH ROW
BEGIN
    SET NEW.entry_id = CONCAT('EID', LPAD(NEW.id, 2, '0'));
END$$

CREATE TRIGGER before_insert_items BEFORE INSERT ON log_items 
FOR EACH ROW
BEGIN
    SET NEW.log_id = CONCAT('LID', LPAD(NEW.id, 2, '0'));
END$$

CREATE TRIGGER update_status_progress BEFORE INSERT ON weight_progress 
FOR EACH ROW
BEGIN
    DECLARE latest_weight DOUBLE;

    -- Get the latest weight for the user
    SELECT current_weight
    INTO latest_weight
    FROM weight_progress
    WHERE username = NEW.username
    ORDER BY date_updated DESC
    LIMIT 1;

    -- Handle first entry case
    IF latest_weight IS NULL THEN
        SET NEW.prev_weight = NEW.current_weight;
    ELSE
        SET NEW.prev_weight = latest_weight;
    END IF;

    -- Determine the status
    IF NEW.current_weight < NEW.prev_weight THEN
        SET NEW.status = 'loss';
    ELSEIF NEW.current_weight > NEW.prev_weight THEN
        SET NEW.status = 'gained';
    ELSE
        SET NEW.status = 'no changes';
    END IF;

    -- Calculate progress and update timestamp
    SET NEW.progress = NEW.prev_weight - NEW.current_weight;
    SET NEW.date_updated = NOW();
END$$

DELIMITER ;

INSERT INTO users(username, password, name, age, sex, height, starting_weight, goal, current_weight, goal_weight, activeness, calorie_goal) VALUES
    ('alamat.taneo', 'taneo123', 'Taneo Uyam', 24, 'Male', 161.0, 75.0, 'Lose Weight', 75.0, 65.0, 'Moderately Active', 1235.80),
    ( 'itsnottomas', 'Tomasito@12', 'Tomas Rodriguez', 28, 'Male', 173.0, 54.0, 'Gain Weight', 54.3, 67.0, 'Extra Active', 2562.56),
    ('magiliwofc', 'Magi0214', 'Magiliw Cruz', 21, 'Female', 157.0, 65.8, 'Lose Weight', 65.8, 56.0, 'Lightly Active', 1735.80);

INSERT INTO diary_entries(username, entry_date, calories_consumed, calories_burned ) VALUES
    ('alamat.taneo', '2024-12-01', 1731.8, 521.0),
    ('itsnottomas', '2024-12-03', 2854.67, 976.0),
    ('magiliwofc', '2024-12-02', 2035.80, 305.0);
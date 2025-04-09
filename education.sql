-- Основные таблицы
CREATE TABLE university (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL
);

CREATE TABLE institute (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    id_university INT REFERENCES university(id)
);

CREATE TABLE department (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    id_institute INT REFERENCES institute(id)
);

CREATE TABLE groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    id_department INT REFERENCES department(id) -- Группа привязана к кафедре
);

CREATE TABLE student (
    student_number VARCHAR(100) PRIMARY KEY,
    fullname VARCHAR(200) NOT NULL,
    id_group INT REFERENCES groups(id)
);

-- Курсы и лекции (с учетом специальных лекций)
CREATE TABLE course (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    id_department INT REFERENCES department(id) -- Курс принадлежит кафедре
);

CREATE TABLE lecture (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    duration_hours INT DEFAULT 2 CHECK (duration_hours = 2), -- 2 академических часа
    is_special BOOLEAN DEFAULT FALSE, -- Флаг специальной лекции
    id_course INT REFERENCES course(id),
    id_original_department INT REFERENCES department(id) -- Для спецлекций: исходная кафедра
);

-- Расписание (связь лекций с группами)
CREATE TABLE schedule (
    id SERIAL PRIMARY KEY,
    id_lecture INT REFERENCES lecture(id),
    id_group INT REFERENCES groups(id),
    timestamp TIMESTAMP NOT NULL
);

-- Посещения (партицированная таблица)
CREATE TABLE attendance (
    id SERIAL,
    timestamp TIMESTAMP NOT NULL,
    week_start DATE NOT NULL,
    id_student VARCHAR(100) REFERENCES student(student_number),
    id_schedule INT REFERENCES schedule(id),
    PRIMARY KEY (id, week_start)
) PARTITION BY RANGE (week_start);

-- Создаем функцию триггера, которая вычисляет week_start
CREATE OR REPLACE FUNCTION set_week_start()
RETURNS TRIGGER AS $$
BEGIN
    NEW.week_start := DATE_TRUNC('week', NEW.timestamp);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Применяем триггер к таблице
CREATE TRIGGER trg_set_week_start
BEFORE INSERT OR UPDATE ON attendance
FOR EACH ROW
EXECUTE FUNCTION set_week_start();

-- Пример партиции
CREATE TABLE attendance_2023_09 PARTITION OF attendance
    FOR VALUES FROM ('2023-09-01') TO ('2023-10-01');

-- Связь с NoSQL (добавляем ключи)
ALTER TABLE student ADD COLUMN redis_key VARCHAR(100);
ALTER TABLE groups ADD COLUMN mongo_id VARCHAR(100);
ALTER TABLE lecture ADD COLUMN elasticsearch_id VARCHAR(100);
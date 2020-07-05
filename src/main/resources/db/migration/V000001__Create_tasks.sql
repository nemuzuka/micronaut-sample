CREATE TABLE tasks (
    task_id VARCHAR (128) NOT NULL,
    task_name VARCHAR (128) NOT NULL,
    content TEXT
);

ALTER TABLE tasks ADD CONSTRAINT tasks_primary PRIMARY KEY(task_id);

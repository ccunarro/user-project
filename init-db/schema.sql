DROP TABLE IF EXISTS tb_user;
DROP TABLE IF EXISTS tb_user_external_project;

CREATE TABLE tb_user
(
    id uuid PRIMARY KEY,
    email VARCHAR(200) UNIQUE NOT NULL,
    password VARCHAR(200) NOT NULL,
    name VARCHAR(120) NULL,
    created TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE tb_user_external_project
(
    id uuid PRIMARY KEY,
    external_project_id VARCHAR(200) UNIQUE NOT NULL,
    user_id uuid NOT NULL REFERENCES tb_user(id) ON DELETE CASCADE,
    created TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS tb_user_external_project_user_id_idx ON tb_user_external_project(user_id);
-- =========================
-- USERS
-- =========================
create table if not exists users (
    id          serial primary key,
    firstname   varchar(255),
    lastname    varchar(255),
    email       varchar(255) unique,
    password    varchar(255),
    role        varchar(50)
);

-- =========================
-- TOKENS
-- =========================
create table if not exists tokens (
    id          serial primary key,
    token       varchar(512) unique,
    token_type  varchar(50) not null,
    revoked     boolean not null default false,
    expired     boolean not null default false,
    user_id     integer,

    constraint fk_tokens_user
        foreign key (user_id)
        references users(id)
        on delete cascade
);

create index if not exists idx_tokens_user_id on tokens(user_id);

-- =========================
-- WEEK
-- =========================
create table if not exists week (
    id          bigserial primary key,
    week_number integer not null,
    month       integer not null,
    year        integer not null,
    start_date  date not null,
    end_date    date not null
);

-- =========================
-- TASK
-- =========================
create table if not exists task (
    id          bigserial primary key,
    title       varchar(255) not null,
    description text,
    status      varchar(50),   -- enum TaskStatus (STRING)
    due_date    date,
    priority    varchar(50),   -- enum Priority (STRING)
    day_of_week varchar(20),   -- enum DayOfWeek (STRING)
    week_id     bigint,

    constraint fk_task_week
        foreign key (week_id)
        references week(id)
        on delete set null
);

create index if not exists idx_task_week_id on task(week_id);

-- =========================
-- REFLECTION
-- =========================
create table if not exists reflection (
    id          bigserial primary key,
    content     text,
    week_id     bigint,

    constraint fk_reflection_week
        foreign key (week_id)
        references week(id)
        on delete cascade
);

create index if not exists idx_reflection_week_id on reflection(week_id);

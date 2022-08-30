create table if not exists authors(
id BIGSERIAL primary key ,
author_name VARCHAR(200) NOT NULL
);

create table if not exists genres(
id BIGSERIAL primary key ,
genre_name VARCHAR(200) NOT NULL
);

create table if not exists books(
id BIGSERIAL primary key ,
title VARCHAR(200) NOT NULL ,
book_text VARCHAR(1000) NOT NULL ,
author_id BIGSERIAL NOT NULL ,
genre_id BIGSERIAL NOT NULL
);

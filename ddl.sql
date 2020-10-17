SET time_zone = 'Asia/Kolkata';

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS games;

CREATE TABLE games(
game_id INT PRIMARY KEY AUTO_INCREMENT,
p1 INT,
p2 INT,
p3 INT,
p4 INT,
score1 INT default 0,
score2 INT default 0,
updated_on datetime default CURRENT_TIMESTAMP,
created_on datetime default CURRENT_TIMESTAMP,
invalidate boolean default false,

CONSTRAINT fk1 FOREIGN KEY (P1) REFERENCES players (player_id),
CONSTRAINT fk2 FOREIGN KEY (P2) REFERENCES players (player_id),
CONSTRAINT fk3 FOREIGN KEY (P3) REFERENCES players (player_id),
CONSTRAINT fk4 FOREIGN KEY (P4) REFERENCES players (player_id)
);

INSERT INTO games(p1, p2, p3, p4, score1, score2) VALUES(1, 2, 3, 4, 19, 21);
INSERT INTO games(p1, p2, p3, p4, score1, score2) VALUES(1, 3, 2, 4, 21, 17);
INSERT INTO games(p1, p2, p3, p4, score1, score2) VALUES(1, 4, 3, 2, 20, 22);

ALTER table games ADD locked boolean default false;



--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS players;

CREATE TABLE players(
player_id INT PRIMARY KEY AUTO_INCREMENT,
name varchar(100) not null,
playing_style varchar(255),
signature_moves varchar(255),
alias varchar(100),
contact varchar(100),
updated_on datetime default CURRENT_TIMESTAMP,
created_on datetime default CURRENT_TIMESTAMP,
invalidate boolean default false
);

INSERT INTO players(name) VALUES('Sam');
INSERT INTO players(name) VALUES('Bob');
INSERT INTO players(name) VALUES('Joy');
INSERT INTO players(name) VALUES('Jim');

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS teams;

CREATE TABLE teams(
team_id INT PRIMARY KEY AUTO_INCREMENT,
name varchar(100) not null,
p1 INT not null,
p2 INT not null,
playing_style varchar(255),
signature_moves varchar(255),
alias varchar(100),
updated_on datetime default CURRENT_TIMESTAMP,
created_on datetime default CURRENT_TIMESTAMP,
invalidate boolean default false,

 FOREIGN KEY (p1) REFERENCES players(player_id),
 FOREIGN KEY (p2) REFERENCES players(player_id)
);

INSERT INTO teams(name, p1, p2) VALUES('Team A', 1, 2);
INSERT INTO teams(name, p1, p2) VALUES('Team B', 3, 4);
INSERT INTO teams(name, p1, p2) VALUES('Team X', 1, 4);
INSERT INTO teams(name, p1, p2) VALUES('Team Y', 2, 3);

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

DROP VIEW games_v2;

CREATE VIEW games_v2
AS 
select g.game_id,
       g.created_on as played_on,
       g.p1, g.p2,
       (select team_id from teams t where (t.p1=g.p1 and t.p2 = g.p2) or (t.p1=g.p2 and t.p2 = g.p1)) as t1,
       g.score1,
       g.p3, g.p4,
       (select team_id from teams t where (t.p1=g.p3 and t.p2 = g.p4) or (t.p1=g.p4 and t.p2 = g.p3)) as t2,
        g.score2,
		g.invalidate
from games g
where g.invalidate=false
order by game_id desc;

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS admin_config;

CREATE TABLE admin_config(
config_id INT PRIMARY KEY AUTO_INCREMENT,
match_lock boolean default false
);

INSERT INTO admin_config(match_lock) VALUES(true);

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------




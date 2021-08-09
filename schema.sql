DROP DATABASE IF EXISTS peaceSystemBuild;

CREATE DATABASE peaceSystemBuild;

USE peaceSystemBuild;


# 직업에 대한 테이블 생성
CREATE TABLE `role`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '사용자 직업 (0 = 에러, 1 = 장례지도사, 2 = 영업자)',
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL
);


# 장례지도사 테이블 (상위/하위 카테고리 없음)
CREATE TABLE director(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	roleLevel SMALLINT(2) UNSIGNED NOT NULL DEFAULT 1 COMMENT 'role 테이블의 id와 연결',
	loginId CHAR(20) NOT NULL UNIQUE,
	loginPw CHAR(60) NOT NULL,
	`name` CHAR(20) NOT NULL,
	cellphoneNo CHAR(20) NOT NULL,
	email CHAR(50) NOT NULL,
	location CHAR(30) NOT NULL,
	`profile` TEXT,
	delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '탈퇴 여부(0 = 유효 회원, 1 = 탈퇴한 회원)',
	delDate DATETIME COMMENT '탈퇴 날짜'
	
);


# 영업자 테이블 ( 하위 카테고리 존재함 ) 
CREATE TABLE businessManager(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	roleLevel SMALLINT(2) UNSIGNED NOT NULL DEFAULT 2 COMMENT 'role 테이블의 id와 연결',
	loginId CHAR(20) NOT NULL UNIQUE,
	loginPw CHAR(60) NOT NULL,
	`name` CHAR(20) NOT NULL,
	cellphoneNo CHAR(20) NOT NULL,
	email CHAR(50) NOT NULL,
	location CHAR(30) NOT NULL,
	`profile` TEXT,
	delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '탈퇴 여부(0 = 유효 회원, 1 = 탈퇴한 회원)',
	delDate DATETIME COMMENT '탈퇴 날짜'
);



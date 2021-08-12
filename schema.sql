DROP DATABASE IF EXISTS peaceSystemBuild;

CREATE DATABASE peaceSystemBuild;

USE peaceSystemBuild;





# 직업에 대한 테이블 생성
CREATE TABLE `role`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '사용자 직업 (0 = 에러, 1 = 관리자, 2 = 회원, 3 = 장례지도사)',
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	roleName CHAR(20) UNIQUE NOT NULL
);

INSERT INTO `role`
SET regDate = NOW(),
updateDate = NOW(),
roleName = '관리자';

INSERT INTO `role`
SET regDate = NOW(),
updateDate = NOW(),
roleName = '회원';

INSERT INTO `role`
SET regDate = NOW(),
updateDate = NOW(),
roleName = '장례지도사';

SELECT * FROM `role`;

# 통합 회원 테이블 생성
CREATE TABLE `member`(
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
	bankAccount CHAR(20) NOT NULL,
	delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '탈퇴 여부(0 = 유효 회원, 1 = 탈퇴한 회원)',
	delDate DATETIME COMMENT '탈퇴 날짜'

);

# 회원 테스트용 더미데이터
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
roleLevel = 1,
loginId = 'admin',
loginPw = 'admin',
`name` = '관리자',
cellphoneNo = '01012341234',
email = 'rkdengus1208@gmail.com',
location = '대전',
bankAccount = "97700608501019";

INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'user1',
loginPw = 'user1',
`name` = '홍길동',
cellphoneNo = '01012341234',
email = 'hong@gmail.com',
location = '서울',
bankAccount = "97700608501019";

SELECT * FROM `member`;



# 출력 테스트용 테이블
CREATE TABLE article(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	title CHAR(30) NOT NULL,
	`body` TEXT NOT NULL
);


INSERT INTO article
SET title = '제목1',
`body` = '내용1';

INSERT INTO article
SET title = '제목2',
`body` = '내용2';

INSERT INTO article
SET title = '제목3',
`body` = '내용3';

INSERT INTO article
SET title = '제목4',
`body` = '내용4';

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
	loginPw CHAR(64) NOT NULL,
	`name` CHAR(20) NOT NULL,
	cellphoneNo CHAR(20) NOT NULL,
	email CHAR(50) NOT NULL,
	location CHAR(30) NOT NULL,
	bank CHAR(20) NOT NULL,
	accountNum CHAR(20) NOT NULL,
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
location = '대전광역시',
bank = '신한',
accountNum = '97700608501019';

INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'user1',
loginPw = 'user1',
`name` = '홍길동',
cellphoneNo = '01012341234',
email = 'hong@gmail.com',
location = '서울특별시',
bank = '신한',
accountNum = '97700608501019';

INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'user999',
loginPw = 'user999',
`name` = '홍길동',
cellphoneNo = '01012341234',
email = 'hongqwe@gmail.com',
location = '서울특별시',
bank = '신한',
accountNum = '97700608501019';

SELECT * FROM `member`;


# member 테이블의 location 칼럼을 위한 department 테이블


CREATE TABLE `department` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` CHAR(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4;




INSERT  INTO `department`(`id`,`name`) VALUES 
(1,'서울특별시'),
(2,'부산광역시'),
(3,'대구광역시'),
(4,'인천광역시'),
(5,'광주광역시'),
(6,'대전광역시'),
(7,'울산광역시'),
(8,'세종특별자치시'),
(9,'경기도'),
(10,'강원도'),
(11,'충청북도'),
(12,'충청남도'),
(13,'전라북도'),
(14,'전라남도'),
(15,'경상북도'),
(16,'경상남도'),
(17,'제주특별자치도');


# member 테이블의 bank 칼럼을 위한 department 테이블
CREATE TABLE `bank` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` CHAR(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4;


INSERT  INTO `bank`(`id`,`name`) VALUES 
(1,'KB국민'),
(2,'우리'),
(3,'신한'),
(4,'SC제일'),
(5,'KEB하나'),
(6,'NH농협'),
(7,'IBK기업'),
(8,'KDB산업');




CREATE TABLE director(
  id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  regDate DATETIME DEFAULT NULL,
  updateDate DATETIME DEFAULT NULL,
  memberId INT(10) UNSIGNED NOT NULL,
  qualification TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '승인 여부(0 = 미승인, 1 = 승인)',
  originFileName VARCHAR(100) NOT NULL, # 업로드 당시의 파일이름
  fileExt CHAR(10) NOT NULL, # 확장자
  typeCode CHAR(20) NOT NULL, # 종류코드 (common)
  type2Code CHAR(20) NOT NULL, # 종류2코드 (attatchment)
  fileSize INT(10) UNSIGNED NOT NULL, # 파일의 사이즈
  fileExtTypeCode CHAR(10) NOT NULL, # 파일규격코드(img, video)
  fileExtType2Code CHAR(10) NOT NULL, # 파일규격2코드(jpg, mp4)
  fileNo SMALLINT(2) UNSIGNED NOT NULL, # 파일번호 (1)
  fileDir CHAR(20) NOT NULL # 파일이 저장되는 폴더명
);

SELECT * FROM director;




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
loginPw = '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',
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
loginPw = '0a041b9462caa4a31bac3567e0b6e6fd9100787db2ab433d96f6d178cabfce90',
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
loginPw = 'db1edbcfb80fd965fe6d8a3aab2a59739255671a0b45dc263d99b9ace81e9e79',
`name` = '홍길동',
cellphoneNo = '01012341234',
email = 'hongqwe@gmail.com',
location = '서울특별시',
bank = '신한',
accountNum = '97700608501019';

SELECT * FROM `member`;

# 회원직업 테이블 생성
CREATE TABLE memberRole(
  id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  memberId INT(10) UNSIGNED NOT NULL,
  roleId INT(10) UNSIGNED NOT NULL,
  introduce LONGTEXT DEFAULT "소개글이 없습니다.",
  authenticationStatus SMALLINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '승인 여부(0 = 미승인, 1 = 승인, 2 = 보류)',
  authenticationDate DATETIME COMMENT '인증된 날짜'
);

SELECT * FROM memberRole;

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




# 파일 테이블 추가
CREATE TABLE genFile (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT, # 번호
  regDate DATETIME DEFAULT NULL, # 작성날짜
  updateDate DATETIME DEFAULT NULL, # 갱신날짜
  delDate DATETIME DEFAULT NULL, # 삭제날짜
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0, # 삭제상태(0:미삭제,1:삭제)
  relTypeCode CHAR(50) NOT NULL, # 관련 데이터 타입(member, buisnessItem ...)
  relId INT(10) UNSIGNED NOT NULL, # 관련 데이터 번호
  originFileName VARCHAR(100) NOT NULL, # 업로드 당시의 파일이름
  fileExt CHAR(10) NOT NULL, # 확장자
  typeCode CHAR(20) NOT NULL, # 종류코드 (common, director, profile)
  type2Code CHAR(20) NOT NULL, # 종류2코드 (attatchment)
  fileSize INT(10) UNSIGNED NOT NULL, # 파일의 사이즈
  fileExtTypeCode CHAR(10) NOT NULL, # 파일규격코드(img, video)
  fileExtType2Code CHAR(10) NOT NULL, # 파일규격2코드(jpg, mp4)
  fileNo SMALLINT(2) UNSIGNED NOT NULL, # 파일번호 (1)
  fileDir CHAR(20) NOT NULL, # 파일이 저장되는 폴더명
  PRIMARY KEY (id),
  KEY relId (relId,relTypeCode,typeCode,type2Code,fileNo)
);

SELECT * FROM genFile;

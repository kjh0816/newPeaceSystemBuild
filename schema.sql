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

INSERT INTO `role`
SET regDate = NOW(),
updateDate = NOW(),
roleName = '물품 공급업자';


SELECT * FROM `role`;

# 통합 회원 테이블 생성
CREATE TABLE MEMBER(
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    roleLevel SMALLINT(2) UNSIGNED NOT NULL DEFAULT 2 COMMENT 'role 테이블의 id와 연결',
    loginId CHAR(20) NOT NULL UNIQUE,
    loginPw CHAR(64) NOT NULL,
    NAME CHAR(20) NOT NULL,
    cellphoneNo CHAR(20) NOT NULL,
    email CHAR(50) NOT NULL,
    location CHAR(30) NOT NULL,
    bank CHAR(20) NOT NULL,
    accountNum CHAR(20) NOT NULL,
    requestStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '신청 여부(0 = 미신청, 1= 신청)',
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
roleLevel = 2,
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
roleLevel = 2,
loginId = 'user2',
loginPw = SHA2('user2', 256),
`name` = '윤길동',
cellphoneNo = '01012341234',
email = 'hong2@gmail.com',
location = '서울특별시',
bank = '신한',
accountNum = '97700608501019';

INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
roleLevel = 2,
loginId = 'user3',
loginPw = SHA2('user3', 256),
`name` = '김지후',
cellphoneNo = '01012341234',
email = 'readshot2@gmail.com',
location = '서울특별시',
bank = '신한',
accountNum = '110222014684';

SELECT * FROM `member`;



# 회원직업 테이블 생성
CREATE TABLE memberRole(
  id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  memberId INT(10) UNSIGNED NOT NULL,
  roleId INT(10) UNSIGNED NOT NULL,
  roleCategoryId SMALLINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '어떤 물품을 공급하는지, 어떤 인력인지(README.md 참조)', 
  introduce LONGTEXT DEFAULT "소개글이 없습니다.",
  authenticationLevel SMALLINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '승인 여부(0 = 미확인, 1 = 승인, 2 = 보류)',
  authenticationDate DATETIME DEFAULT NOW() COMMENT '인증된 날짜'
);




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



# 고인에 대한 정보를 담는 테이블

CREATE TABLE `client`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	memberId INT(10) UNSIGNED NOT NULL COMMENT '현재 로그인한 회원 id(장례지도사를 연결해준 영업자)',
	directorMemberId INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0일 경우, 장례지도사가 아직 미정, 정해지면 장례지도사 id를 넣는다.',
	deceasedName CHAR(20) NOT NULL COMMENT '고인의 성함',
	relatedName CHAR(20) NOT NULL COMMENT '상주 또는 유족의 성함',
	cellphoneNo CHAR(20) NOT NULL COMMENT '위 성함분의 연락처',
	location CHAR(30) NOT NULL,
	address CHAR(30) NOT NULL COMMENT '장례지도사가 찾아갈 수 있도록'
);


SELECT * FROM CLIENT;








# '제단꽃 표준' 테이블
# 
CREATE TABLE flower(  
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(15) UNIQUE NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	standardPrice CHAR(10) NOT NULL COMMENT '기준가',
	costPrice CHAR(10) NOT NULL COMMENT '원가',
	height CHAR(10) NOT NULL COMMENT '세로 길이 (단위: mm)',
	width CHAR(10) NOT NULL COMMENT '가로 길이 (단위: mm)'
);


INSERT INTO flower
SET regDate = NOW(),
updateDate = NOW(),
`name` = '1호',
retailPrice = '150000',
standardPrice = '130000',
costPrice = '100000',
height = '1500',
width = '600';


INSERT INTO flower
SET regDate = NOW(),
updateDate = NOW(),
`name` = '2호',
retailPrice = '160000',
standardPrice = '140000',
costPrice = '110000',
height = '1650',
width = '999';


SELECT 
`name`,
FORMAT(`retailPrice` , 0) AS `retailPrice`,
FORMAT(`standardPrice` , 0) AS `standardPrice`,
FORMAT(`costPrice` , 0) AS `costPrice`,
height,
width
FROM flower;



SELECT CONCAT(
SUBSTR(cellphoneNo, 1, 3)
, '-'
, SUBSTR(cellphoneNo, 4, 4)
, '-'
, SUBSTR(cellphoneNo, 8, 4)
) AS `cellphoneNo`
FROM `member`;


# 진행하는 장례에 대한 funeral 테이블
# funeral 테이블에 최초로 데이터가 들어가는 시점은, client에 대한 장례지도사가 정해졌을 때
# 물품공급업, 인력 공급업 테이블이 추가될 때마다 칼럼이 추가돼야 한다.

CREATE TABLE funeral(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	clientId INT(10) UNSIGNED NOT NULL,
	directorMemberId INT(10) UNSIGNED NOT NULL,
	memberId INT(10) UNSIGNED NOT NULL,
	flowerId INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음. 선택된 flower의 id',
	progress SMALLINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '장례 진행 상태'
);

/*
insert into funeral
set regDate = now(),
updateDate = now(),
clientId = 1,
directorMemberId = 3,
memberId = 2,
flowerId = 1;
*/
SELECT * FROM funeral;


#더미데이터 추가하는 부분
# 테스트 회원 장례지도사 신청 더미데이터
/*
INSERT INTO memberRole
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
roleId = 3,
authenticationLevel = 0,
authenticationDate = NOW();

INSERT INTO memberRole
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
roleId = 3,
authenticationLevel = 0,
authenticationDate = NOW();

INSERT INTO memberRole
SET regDate = NOW(),
updateDate = NOW(),
memberId = 3,
roleId = 3,
authenticationLevel = 0,
authenticationDate = NOW();

INSERT INTO memberRole
SET regDate = NOW(),
updateDate = NOW(),
memberId = 4,
roleId = 3,
authenticationLevel = 0,
authenticationDate = NOW();

UPDATE `member` SET roleLevel = 3;
UPDATE `member` SET roleLevel = 1 WHERE id = 1;
*/
/*
INSERT INTO genFile
SET regDate = NOW(),
updateDate = NOW(),
relTypeCode = "member",
relId = 1,
originFileName = "제목없음.png",
fileExt = "png",
typeCode = "director",
type2Code = "attachment",
fileSize = 6180,
fileExtTypeCode = "img",
fileExtType2Code = "png",
fileNo = 1,
fileDir = "2021_08";


INSERT INTO genFile
SET regDate = NOW(),
updateDate = NOW(),
relTypeCode = "member",
relId = 2,
originFileName = "제목없음.png",
fileExt = "png",
typeCode = "director",
type2Code = "attachment",
fileSize = 6180,
fileExtTypeCode = "img",
fileExtType2Code = "png",
fileNo = 1,
fileDir = "2021_08";

INSERT INTO genFile
SET regDate = NOW(),
updateDate = NOW(),
relTypeCode = "member",
relId = 3,
originFileName = "제목없음.png",
fileExt = "png",
typeCode = "director",
type2Code = "attachment",
fileSize = 6180,
fileExtTypeCode = "img",
fileExtType2Code = "png",
fileNo = 1,
fileDir = "2021_08";

INSERT INTO genFile
SET regDate = NOW(),
updateDate = NOW(),
relTypeCode = "member",
relId = 4,
originFileName = "제목없음.png",
fileExt = "png",
typeCode = "director",
type2Code = "attachment",
fileSize = 6180,
fileExtTypeCode = "img",
fileExtType2Code = "png",
fileNo = 1,
fileDir = "2021_08";
*/
/*
# @mid, @genMid 변수는 한번만 실행
SET @mid = 5;
SET @genMid = @mid;

INSERT INTO `member` (regDate, updateDate, roleLevel, loginId, loginPw, `name`, cellphoneNo, email, location, bank, accountNum, requestStatus)
SELECT NOW(), NOW(), 3, CONCAT("user", RAND()), CONCAT("user", RAND()), CONCAT("user", RAND()), "01012312312", CONCAT("user", RAND(), "@naveer.com"), "대전", "신한", "123123123123", 1
FROM `member`;

insert into memberRole (regDate, updateDate, memberId, roleId, authenticationLevel, authenticationDate)
select now(), now(), @mid := @mid + 1, 3, 0, now()
from memberRole;

insert into genFile (regDate, updateDate, relTypeCode, relId, originFileName, fileExt, typeCode, type2Code, fileSize, fileExtTypeCode, fileExtType2Code, fileNo, fileDir)
select now(), now(), "member", @genMid := @genMid + 1, "제목없음.png", "png", "director", "attachment", 6180, "img", "png", 1, "2021_08"
from genFile;
*/






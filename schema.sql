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

INSERT INTO `role`
SET regDate = NOW(),
updateDate = NOW(),
roleName = '도우미';

SELECT * FROM `role`;



# 통합 회원 테이블 생성
# authKey = 66자
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
loginPw = SHA2('admin', 256),
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
loginPw = SHA2('user1', 256),
`name` = '홍길동',
cellphoneNo = '01049219810',
email = 'readshot2@gmail.com',
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
cellphoneNo = '01049219810',
email = 'hong2@gmail.com',
location = '서울특별시',
bank = '신한',
accountNum = '97700608501019';

INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
roleLevel = 2,
loginId = 'user3333',
loginPw = SHA2('user3', 256),
`name` = '김지후',
cellphoneNo = '01049219810',
email = 'readshot2@gmail.com',
location = '서울특별시',
bank = '신한',
accountNum = '110222014684';

INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
roleLevel = 2,
loginId = 'user4',
loginPw = SHA2('user4', 256),
`name` = '강두현',
cellphoneNo = '01012341234',
email = 'rkdengus1208@gmail.com',
location = '서울특별시',
bank = '신한',
accountNum = '12341234123';








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



# user2 를 운구차 운전자로
UPDATE `member`
SET roleLevel = 4
WHERE id = 3;


INSERT INTO memberRole
SET regDate = NOW(),
updateDate = NOW(),
memberId = 3,
roleId = 4,
roleCategoryId = 3,
authenticationLevel = 1;






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


CREATE TABLE funeralHall(
	id INT(10),
	`name` CHAR(60) NOT NULL,
	department CHAR(30) NOT NULL,
	departmentDetail CHAR(40) NOT NULL,
	address CHAR(120) NOT NULL,
	cellphoneNo CHAR(20) NOT NULL
);

SELECT * FROM funeralHall;




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
	location CHAR(30) NOT NULL,
	# 고인 관련 정보
	deceasedName CHAR(20) NOT NULL COMMENT '고인의 성함',
	sex CHAR(1) NOT NULL DEFAULT 'N'COMMENT '고인의 성별 N = null, M = male, F = female',
	briefAddress CHAR(100) NOT NULL COMMENT '영업자가 최초에 입력하는 주소(장례지도사 본인이 가까운지 등을 파악하기 위한 용도)',
	deceasedAddress CHAR(100) NOT NULL DEFAULT '' COMMENT '운구차가 찾아가기 위한 주소',
	deceasedHomeAddress CHAR(100) NOT NULL DEFAULT '' COMMENT '장례지도사가 서류상 대조할 수 있도록',
	deceasedDate CHAR(20) NOT NULL DEFAULT '' COMMENT '사망일',
	deceasedLunar CHAR(1) NOT NULL DEFAULT 'N' COMMENT '기일이 음력인지 아닌지(N = null, M = 양력, F = 음력)',
	frontNum CHAR(60) NOT NULL DEFAULT '' COMMENT '고인의 주민등록번호 앞자리',
	backNum CHAR(60) NOT NULL DEFAULT '' COMMENT '고인의 주민등록번호 뒷자리',
	birth CHAR(20) NOT NULL DEFAULT '' COMMENT '고인의 생년월일',
	birthLunar CHAR(1) NOT NULL DEFAULT 'N' COMMENT '생년월일이 음력인지 아닌지(N = null, M = 양력, F = 음력)',
	funeralHall CHAR(50) NOT NULL DEFAULT '' COMMENT '장례식장 이름',
	funeralHallRoom CHAR(20) NOT NULL DEFAULT '' COMMENT '장례식장 호실',
	familyClan CHAR(20) NOT NULL DEFAULT '' COMMENT '고인의 본관(시조의 고향)',
	religion CHAR(20) NOT NULL DEFAULT '' COMMENT '종교',
	duty CHAR(30) NOT NULL DEFAULT '' COMMENT '직분',
	funeralMethod CHAR(1) NOT NULL DEFAULT 'N' COMMENT '장법(매장인지, 화장인지) N = null, M = 매장, L = 화장',
	cremationLocation CHAR(100) NOT NULL DEFAULT '' COMMENT '화장장',
	buryLocation CHAR(100) NOT NULL DEFAULT '' COMMENT '매장지(매장/화장 둘 다 필요)',
	cause SMALLINT(1) NOT NULL DEFAULT 0 COMMENT '사망 원인(병사 = 0, 사고사 = 1, 기타 = 2)',
	papers CHAR(1) NOT NULL DEFAULT 'N' COMMENT '사망 서류(N = null, 진단서 = M, 사체검안서 = F)',
	autopsyCheck CHAR(1) NOT NULL DEFAULT 'N' COMMENT '검시필증(사체검안서에 대해 추가로 필요할 수 있음)N = null, F = 없음, M = 있음',
	casketDate CHAR(20) NOT NULL DEFAULT '' COMMENT '입관날짜',
	casketTime CHAR(20) NOT NULL DEFAULT '' COMMENT '입관시간(분까지)',
	leavingDate CHAR(20) NOT NULL DEFAULT '' COMMENT '발인날짜',
	leavingTime CHAR(20) NOT NULL DEFAULT '' COMMENT '발인시간(분까지)'
);





SELECT * FROM `client`;



# client(고인)에 대한 유족 family 테이블
# 상주인 경우에만 address, bank, accountNum, accoutOwner를 받는다.
# 상주는 id가 1인 row
CREATE TABLE family(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	clientId INT(10) UNSIGNED NOT NULL,
	chiefStatus TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '상주인지 아닌지(상주 = 1)',
	`name` CHAR(20) NOT NULL,
	relation CHAR(30) NOT NULL DEFAULT '' COMMENT '고인과의 관계(고인으로부터 누구인지 ex) 아들)',
	cellphoneNo CHAR(20) NOT NULL,
	address CHAR(100) NOT NULL DEFAULT '' COMMENT '상주만 집주소를 입력 받고 저장한다.',
	bank CHAR(20) NOT NULL DEFAULT '' COMMENT '유족이 부조금을 받을 수 있는 계좌',
	accountNum CHAR(20) NOT NULL DEFAULT '',
	accountOwner CHAR(20) NOT NULL DEFAULT ''
);






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


ALTER TABLE funeral ADD COLUMN flowerTributeId INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER flowerId;
ALTER TABLE funeral ADD COLUMN femaleMourningClothBlackId INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER flowerTributeId;
ALTER TABLE funeral ADD COLUMN femaleMourningClothWhiteId INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER femaleMourningClothBlackId;
ALTER TABLE funeral ADD COLUMN maleMourningClothId INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER femaleMourningClothWhiteId;
ALTER TABLE funeral ADD COLUMN shirtId INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER maleMourningClothId;
ALTER TABLE funeral ADD COLUMN necktieId INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER shirtId;
ALTER TABLE funeral ADD COLUMN coffinTransporterUseStatus TINYINT(1) NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER necktieId;
ALTER TABLE funeral ADD COLUMN shroudId INT(10) NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER coffinTransporterUseStatus;
ALTER TABLE funeral ADD COLUMN coffinId INT(10) NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER shroudId;
ALTER TABLE funeral ADD COLUMN incenseId INT(10) NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER coffinId;
ALTER TABLE funeral ADD COLUMN candleId INT(10) NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER incenseId;
ALTER TABLE funeral ADD COLUMN ancestralTabletId INT(10) NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER candleId;
ALTER TABLE funeral ADD COLUMN condolenceMoneyBookId INT(10) NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER ancestralTabletId;
ALTER TABLE funeral ADD COLUMN condolenceBookId INT(10) NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER condolenceMoneyBookId;
ALTER TABLE funeral ADD COLUMN pictureRibbonId INT(10) NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음' AFTER condolenceBookId;


SELECT * FROM funeral;
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

CREATE TABLE `order`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	vendorMemberId INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '상품 등록한 사업자 회원번호 0일경우 아직 미정',
	funeralId INT(10) UNSIGNED NOT NULL COMMENT 'funeral 테이블이랑 이어진 ID값',
	roleCategoryId SMALLINT(1) UNSIGNED NOT NULL COMMENT '어떤 상품인지 EX) 1 = 제단꽃',
	standardId INT(10) UNSIGNED NOT NULL COMMENT '스탠다드 상품의 번호',
	orderStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '지도사가 오더를 넣으면 기본값 0 업자가 오더를 받으면 1로 변경',
	completionStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '기본값 0 업자가 오더를 받고 서비스를 완료하면 1로 변경',
	detail CHAR(30) NOT NULL
);

SELECT * FROM `order`;

# 헌화 스텐다드 테이블
CREATE TABLE flowerTribute(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가',
	bunch INT(10) UNSIGNED NOT NULL COMMENT '한묶음당 몇송이인지'
);

INSERT INTO flowerTribute
SET regDate = NOW(),
updateDate = NOW(),
retailPrice = "1500",
costPrice = "1000",
bunch = 30;

SELECT * FROM flowerTribute;

# order 테이블과 조인해서 사용할 헌화만의 스페셜한 정보를 가진 order 테이블
# 차차 추가될 상품들에 각각 담길 주문정보들이 서로 많이 다를것 같아서 기본 order 테이블을 두고 서브 테이블로 조인하는 방식을 채택함.
CREATE TABLE flowerTributeOrder(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	bunchCnt INT(10) UNSIGNED NOT NULL COMMENT '몇묶음인지',
	packing TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '포장 여부 0 = 미포장, 1 = 포장'
);


CREATE TABLE flowerOrder(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL
);

CREATE TABLE femaleMourningClothBlackOrder(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	femaleClothCnt INT(10) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE femaleMourningClothWhiteOrder(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	femaleClothCnt INT(10) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE maleMourningClothOrder(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	maleClothCnt INT(10) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE shirtOrder(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	shirtCnt INT(10) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE necktieOrder(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	necktieCnt INT(10) UNSIGNED NOT NULL DEFAULT 0
);

SELECT * FROM `order`;

CREATE TABLE femaleMourningClothBlack(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(10) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

CREATE TABLE femaleMourningClothWhite(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(10) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

CREATE TABLE maleMourningCloth(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(10) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

CREATE TABLE shirt(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(10) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

CREATE TABLE necktie(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(10) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

INSERT INTO femaleMourningClothBlack
SET regDate = NOW(),
updateDate = NOW(),
`name` = "여성상복(흑)",
retailPrice = "20000",
costPrice = "5000";

INSERT INTO femaleMourningClothWhite
SET regDate = NOW(),
updateDate = NOW(),
`name` = "여성상복(백)",
retailPrice = "20000",
costPrice = "8000";

INSERT INTO maleMourningCloth
SET regDate = NOW(),
updateDate = NOW(),
`name` = "남성상복",
retailPrice = "30000",
costPrice = "15000";

INSERT INTO shirt
SET regDate = NOW(),
updateDate = NOW(),
`name` = "와이셔츠",
retailPrice = "15000",
costPrice = "10000";

INSERT INTO necktie
SET regDate = NOW(),
updateDate = NOW(),
`name` = "넥타이",
retailPrice = "5000",
costPrice = "1000";

SELECT * FROM femaleMourningClothBlack;
SELECT * FROM femaleMourningClothWhite;
SELECT * FROM maleMourningCloth;
SELECT * FROM shirt;
SELECT * FROM necktie;








CREATE TABLE coffinTransporter(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	funeralId INT(10) UNSIGNED NOT NULL,
	memberId INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '담당 운구차 운전자, 0 = 아직 정해지지 않음',
	price CHAR(20) NOT NULL DEFAULT '70000' COMMENT '가격',
	departureAddress CHAR(100) NOT NULL,
	destinationAddress CHAR(100) NOT NULL,
	completionStatus TINYINT(1) NOT NULL DEFAULT 0 COMMENT '운구 완료 여부(0 = 미완료, 1 = 완료)'
);





SELECT * FROM coffinTransporter;




# 도우미 테이블
# funeralId, helperMemberId가 같더라도, work 관련 칼럼이 다름으로 하나로 묶어진 여러 일이 구분될 수 있다.
CREATE TABLE helper(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	funeralId INT(10) UNSIGNED NOT NULL,
	helperMemberId INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0 = 아직 정해지지 않음',
	helperMemberCount INT(10) UNSIGNED NOT NULL COMMENT '해당 근무에 몇 명이 필요한지',
	helperPackageId INT(10) UNSIGNED NOT NULL COMMENT '일감을 묶어주기 위한 칼럼',
	department CHAR(20) NOT NULL,
	workDate CHAR(20) NOT NULL,
	workStartTime CHAR(10) NOT NULL COMMENT '10:00, 12:00',
	workFinishTime CHAR(10) NOT NULL COMMENT '20:00, 22:00',
	additionalWorkHour SMALLINT(1) NOT NULL DEFAULT 0  COMMENT'추가 근무 시간(시간 단위)',
	pay CHAR(20) NOT NULL DEFAULT '9000' COMMENT '시급',
	additionalPay CHAR(20) NOT NULL DEFAULT '15000' COMMENT '추가근무 시급',
	completionStatus TINYINT(0) NOT NULL DEFAULT 0 COMMENT '0 = 미완료, 1 = 완료'
);

SELECT * FROM helper;


# helper 테이블에서 분리된 업무들을 묶어주기 위한 테이블
CREATE TABLE helperPackage(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	helperNum SMALLINT(1) UNSIGNED NOT NULL
);





CREATE TABLE shroud(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(15) NOT NULL,
	gender CHAR(1) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

INSERT INTO shroud
SET regDate = NOW(),
updateDate = NOW(),
`name` = '갖은수의(혼합사)',
gender = 'M',
retailPrice = '200000',
costPrice = '80000';

INSERT INTO shroud
SET regDate = NOW(),
updateDate = NOW(),
`name` = '갖은수의(혼합사)',
gender = 'F',
retailPrice = '200000',
costPrice = '80000';

INSERT INTO shroud
SET regDate = NOW(),
updateDate = NOW(),
`name` = '갖은수의(저마)',
gender = 'M',
retailPrice = '500000',
costPrice = '150000';

INSERT INTO shroud
SET regDate = NOW(),
updateDate = NOW(),
`name` = '갖은수의(저마)',
gender = 'F',
retailPrice = '500000',
costPrice = '150000';

INSERT INTO shroud
SET regDate = NOW(),
updateDate = NOW(),
`name` = '갖은수의(대마)',
gender = 'M',
retailPrice = '1200000',
costPrice = '380000';

INSERT INTO shroud
SET regDate = NOW(),
updateDate = NOW(),
`name` = '갖은수의(대마)',
gender = 'F',
retailPrice = '1200000',
costPrice = '380000';

INSERT INTO shroud
SET regDate = NOW(),
updateDate = NOW(),
`name` = '갖은수의(인견+대마)',
gender = 'M',
retailPrice = '1500000',
costPrice = '450000';

INSERT INTO shroud
SET regDate = NOW(),
updateDate = NOW(),
`name` = '갖은수의(인견+대마)',
gender = 'F',
retailPrice = '1500000',
costPrice = '450000';

INSERT INTO shroud
SET regDate = NOW(),
updateDate = NOW(),
`name` = '안동포',
gender = 'M',
retailPrice = '5000000',
costPrice = '4800000';

INSERT INTO shroud
SET regDate = NOW(),
updateDate = NOW(),
`name` = '안동포',
gender = 'F',
retailPrice = '5000000',
costPrice = '4800000';

SELECT * FROM shroud;

CREATE TABLE shroudOrder(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL
);

# 빈소용품(초동용품)
CREATE TABLE `incense`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(15) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

INSERT INTO `incense`
SET regDate = NOW(),
updateDate = NOW(),
`name` = '기본 향',
retailPrice = '30000',
costPrice = '10000';

CREATE TABLE `incenseOrder` (
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	incenseCnt INT(10) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE `candle`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(15) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

INSERT INTO `candle`
SET regDate = NOW(),
updateDate = NOW(),
`name` = '기본 양초',
retailPrice = '15000',
costPrice = '6000';

CREATE TABLE `candleOrder` (
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	candleCnt INT(10) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE `ancestralTablet`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(15) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

INSERT INTO `ancestralTablet`
SET regDate = NOW(),
updateDate = NOW(),
`name` = '기본 위패',
retailPrice = '30000',
costPrice = '15000';

CREATE TABLE `ancestralTabletOrder` (
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	ancestralTabletCnt INT(10) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE `condolenceMoneyBook`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(15) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

INSERT INTO `condolenceMoneyBook`
SET regDate = NOW(),
updateDate = NOW(),
`name` = '기본 부의록',
retailPrice = '15000',
costPrice = '8000';

CREATE TABLE `condolenceMoneyBookOrder` (
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	condolenceMoneyBookCnt INT(10) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE `condolenceBook`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(15) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

INSERT INTO `condolenceBook`
SET regDate = NOW(),
updateDate = NOW(),
`name` = '기본 조의록',
retailPrice = '15000',
costPrice = '8000';

CREATE TABLE `condolenceBookOrder` (
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	condolenceBookCnt INT(10) UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE `pictureRibbon`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`name` CHAR(15) NOT NULL,
	retailPrice CHAR(10) NOT NULL COMMENT '소비자가',
	costPrice CHAR(10) NOT NULL COMMENT '원가'
);

INSERT INTO `pictureRibbon`
SET regDate = NOW(),
updateDate = NOW(),
`name` = '기본 사진리본',
retailPrice = '5000',
costPrice = '1500';

CREATE TABLE `pictureRibbonOrder` (
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL,
	pictureRibbonCnt INT(10) UNSIGNED NOT NULL DEFAULT 0
);

# 관
CREATE TABLE coffin(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`name` CHAR(20) NOT NULL,
	size CHAR(20) NOT NULL,
	chi CHAR(20) NOT NULL,
	costPrice CHAR(20) NOT NULL,
	retailPrice CHAR(20) NOT NULL
);



INSERT INTO coffin
SET `name` = '오동나무',
size = '보통',
chi = '0.6',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '오동나무',
size = '특',
chi = '0.6',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '오동나무',
size = '보통',
chi = '1.0',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '오동나무',
size = '특',
chi = '1.0',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '오동나무',
size = '맞춤',
chi = '1.0',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '오동나무',
size = '보통',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';


INSERT INTO coffin
SET `name` = '오동나무',
size = '특',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '오동나무',
size = '맞춤',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';


INSERT INTO coffin
SET `name` = '오동나무',
size = '2단보통',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '오동나무',
size = '2단특',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '오동나무',
size = '보통',
chi = '2.0',
costPrice = '10000',
retailPrice = '20000';


INSERT INTO coffin
SET `name` = '오동나무',
size = '특',
chi = '2.0',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '오동나무',
size = '맞춤',
chi = '2.0',
costPrice = '10000',
retailPrice = '20000';


INSERT INTO coffin
SET `name` = '오동나무',
size = '2단보통',
chi = '2.0',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '오동나무',
size = '2단특',
chi = '2.0',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '솔송',
size = '보통',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '솔송',
size = '특',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '솔송',
size = '맞춤',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';


INSERT INTO coffin
SET `name` = '솔송',
size = '2단보통',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '솔송',
size = '2단특',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';


INSERT INTO coffin
SET `name` = '향송',
size = '보통',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '향송',
size = '특',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '향송',
size = '맞춤',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '향송',
size = '2단보통',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';

INSERT INTO coffin
SET `name` = '향송',
size = '2단특',
chi = '1.5',
costPrice = '10000',
retailPrice = '20000';




CREATE TABLE coffinOrder(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	orderId INT(10) UNSIGNED NOT NULL
);


SELECT * FROM shroudOrder;
SELECT * FROM coffinOrder;


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

# Progress 페이지 Order 정보 초기화
/*
UPDATE funeral SET flowerId = 0;
UPDATE funeral SET flowerTributeId = 0;
UPDATE funeral SET femaleMourningClothId = 0;
UPDATE funeral SET maleMourningClothId = 0;
UPDATE funeral SET shirtId = 0;
UPDATE funeral SET necktieId = 0;
UPDATE funeral SET incenseId = 0;
DELETE FROM `order`;
DELETE FROM `flowerTributeOrder`;
DELETE FROM `flowerOrder`;
DELETE FROM `femaleMourningClothOrder`;
DELETE FROM `maleMourningClothOrder`;
DELETE FROM `shirtOrder`;
DELETE FROM `necktieOrder`;
DELETE FROM `incenseOrder`;
*/
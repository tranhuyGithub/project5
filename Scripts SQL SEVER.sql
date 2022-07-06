create database CharityWebsite
use CharityWebsite
create table ACCOUNT(
	ACCOUNT_ID int identity(1,1) not null,
	USERNAME varchar(50) not null,
	ACCOUNT_PASSWORD varchar(50) not null,
	EMAIL varchar(200) not null,
	AGE int not null,
	ACCOUNT_ROLE int not null,
	constraint PK_ACCOUNT primary key(ACCOUNT_ID)
);
Create table CHARITY(
	CHARITY_ID int identity(1,1) not null,
	CHARITY_NAME nvarchar(200) not null, 
	CHARITY_CONTENT nvarchar(max) not null,
	CHARITY_START datetime not null, 
	CHARITY_END datetime not null,
	CHARITY_TARGET money not null, 
	CHARITY_PROCESS money not null,
	PAY_METHOD nvarchar(200) not null,
	ACCOUNT_ID int not null,
	CHARITY_IMG nvarchar(200) null, 
	CHARITY_STATUS bit not null default(1), 
	constraint PK_CHARITY primary key(CHARITY_ID),
	constraint FK_CHARITY_ACCOUNT foreign key(ACCOUNT_ID) references ACCOUNT(ACCOUNT_ID)
);
Create table CHARITY_DETAIL(
	ID_CHARITY_DETAIL int identity(1,1) not null,
	CHARITY_ID int not null,
	ACCOUNT_ID int not null, 
	CASH money not null, 
	DATE_PAY datetime default(getdate()), 
	PAY_METHOD nvarchar(200) not null,
	constraint PK_CHARITY_DETAIL primary key (ID_CHARITY_DETAIL),
	constraint FK_CHARITY_DETAIL_ID foreign key (CHARITY_ID) references CHARITY(CHARITY_ID),
	constraint FK_CHARITY_DETAIL_ACCOUNT foreign key(ACCOUNT_ID) references ACCOUNT(ACCOUNT_ID)
);
CREATE table NEWS(
	ID_NEWS int identity(1,1) not null,
	TITLE nvarchar(200) not null,
	CONTENT nvarchar(max) not null, 
	NEWS_DATE datetime not null,
	ACCOUNT_ID int not null,
	COUNTVIEW int not null,
	NEWS_IMG nvarchar(200) null,
	constraint PK_NEWS primary key (ID_NEWS),
	
);

SET IDENTITY_INSERT ACCOUNT ON
insert into ACCOUNT (ACCOUNT_ID, USERNAME, ACCOUNT_PASSWORD, EMAIL, AGE, ACCOUNT_ROLE)
VALUES(1, 'admin', 'admin', 'admin@gmail.com', 23,0 );
insert into ACCOUNT (ACCOUNT_ID, USERNAME, ACCOUNT_PASSWORD, EMAIL, AGE, ACCOUNT_ROLE)
VALUES(2, 'tranhuy', '123456','tranhuy@gmail.com', 23 , 1 );
insert into ACCOUNT (ACCOUNT_ID, USERNAME, ACCOUNT_PASSWORD, EMAIL, AGE, ACCOUNT_ROLE)
VALUES(3, 'nhatminh','123456', 'nhatminh@gmail.com', 22, 1);
insert into ACCOUNT (ACCOUNT_ID, USERNAME, ACCOUNT_PASSWORD, EMAIL, AGE, ACCOUNT_ROLE)
VALUES(4, 'tuandat','123456', 'tuandat@gmail.com', 23,1 );
insert into ACCOUNT (ACCOUNT_ID, USERNAME, ACCOUNT_PASSWORD, EMAIL, AGE, ACCOUNT_ROLE)
VALUES(5, 'tronghieu', '123456', 'tronghieu@gmail.com', 25,1);

SET IDENTITY_INSERT ACCOUNT OFF

SET IDENTITY_INSERT CHARITY ON
insert into CHARITY (CHARITY_ID, CHARITY_NAME, CHARITY_CONTENT, CHARITY_START, CHARITY_END, CHARITY_TARGET, CHARITY_PROCESS , PAY_METHOD, ACCOUNT_ID, CHARITY_IMG, CHARITY_STATUS) 
VALUES(1, 'Chung tay quyên góp kiến tạo công viên đầu tiên dành cho cộng đồng nuôi chó tại Công viên xanh Yên Sở',
'Chung tay cùng với Think Playgrounds để kiến tạo một không gian vui chơi thân thiện dành cho cộng đồng nuôi chó tại công viên Yên Sở.
Các lợi ích chính của công viên dành cho cộng đồng nuôi chó:

Làm cho công viên trở nên an toàn hơn cho những người sử dụng
Góp phần đảm bảo vệ sinh môi trường trong công viên
Xây dựng cộng đồng thân thiện và văn mình
Chi phí vận hành thấp.
Chung tay gây quỹ xây dựng công viên cho cộng đồng nuôi chó tại Công viên xanh Yên Sở

Để kiến tạo một mô hình công viên dành cho cộng đồng nuôi chó đầu tiên tại Hà Nội, Trái tim MoMo hợp tác cùng Think Playgrounds đã phối hợp với ông Gabe No và công viên Yên Sở Hà Nội trao đổi, phát triển thiết kế và xây dựng công viên cho cộng đồng nuôi chó. Nhằm tạo một môi trường vui chơi, đảm bảo an toàn giữa con người và thú cưng.
', '2022-1-4','2022-11-4', 100000000 , 26000000 , 'Cash Or Paypal Sandbox', 2, 'https://static.mservice.io/blogscontents/momo-upload-api-220505102249-637873429696827599.jpg'
,'True'
);

insert into CHARITY (CHARITY_ID, CHARITY_NAME, CHARITY_CONTENT, CHARITY_START, CHARITY_END, CHARITY_TARGET, CHARITY_PROCESS , PAY_METHOD, ACCOUNT_ID, CHARITY_IMG, CHARITY_STATUS)
values(2, 'Chung tay quyên góp xây dựng và cải tạo điểm trường Bản Yên - tỉnh Điện Biên', 
'Cùng chung tay quyên góp xây dựng điểm trường Bản Yên - tỉnh Điện Biên để các bé không phải học trong phòng tạm bằng tôn đã hư hỏng nhiều, oi nóng và bức bối.
Tình cảnh phải chịu sự nóng bức và oi ả của thầy trò Bản Yên – tỉnh Điện Biên

Trường Mầm non Bản Yên - xã Luân Giói - huyện Điện Biên Đông - tỉnh Điện Biên là một bản nhỏ, khuất sau những dãy núi của miền Tây Bắc. Nơi đây có hơn 300 đồng bào người Thái sinh sống, chủ yếu đều thuộc hộ nghèo. Đời sống dân bản còn nhiều khó khăn, trình độ dân trí còn hạn chế, nên việc cải tạo và xây dựng môi trường học tập, quan tâm đến giáo dục của trẻ nhỏ vẫn còn gặp nhiều trở ngại.

Thấu hiểu tấm lòng của thầy cô các em nhỏ ở đây luôn chăm ngoan, hiếu học. Mỗi ngày các em đều đều đặn đến trường học tập, vui chơi cùng thầy cô và các bạn. Trong những ánh mắt ngây thơ ấy, ngoài những khó khăn của cuộc sống còn luôn luôn bừng lên khát khao chinh phục tri thức.
','2022-3-22','2022-10-22', 250000000, 70000000, 'Cash Or Paypal Sandbox',3, 'https://static.mservice.io/blogscontents/momo-upload-api-220429102427-637868246679840637.jpg','true'
);

insert into CHARITY (CHARITY_ID, CHARITY_NAME, CHARITY_CONTENT, CHARITY_START, CHARITY_END, CHARITY_TARGET, CHARITY_PROCESS , PAY_METHOD, ACCOUNT_ID, CHARITY_IMG, CHARITY_STATUS)
values(3, 'Chung tay xây thêm phòng học tại điểm trường Tiểu học Tá Miếu cho các em nhỏ ở cực Tây Tổ quốc', 
'Điểm trường Mầm non và Tiểu học Tá Miếu là nơi học tập của 71 em nhỏ, chủ yếu là người Hà Nhì, nay đã sụt lún, xuống cấp cần được nâng cấp, sửa chữa.

Xót thương cho hoàn cảnh thiếu thốn của các em nhỏ điểm trường Tá Miếu, tỉnh Điện Biên

Cách Hà Nội gần 700km, Tá Miếu (xã Sín Thầu, huyện Mường Nhé, Điện Biên) là bản biên giới, nơi con gà gáy người dân ba nước đều nghe. Xa xôi, cách trở, bản chỉ có 55 hộ gia đình với hơn 200 nhân khẩu, trong đó bà con chủ yếu là người dân tộc Hà Nhì, cuộc sống còn nhiều khó khăn.
Cùng chung tay đóng góp xây thêm phòng học đợt 1 cho các em nhỏ Tiểu học Tá Miếu

Thấu hiểu mong mỏi của thầy cô và học sinh của bản, Quỹ Hy Vọng kêu gọi sự chung tay mang tới bản Tá Miếu ngôi trường rộng rãi, khang trang và an toàn hơn cho 71 em học sinh, giúp các em tiếp tục giấc mơ đi học, thầy cô có thêm niềm tin và nghị lực vượt qua những khó khăn trong hành trình gieo con chữ.

','2022-4-02','2022-12-02', 200000000, 120000000, 'Cash Or Paypal Sandbox',4, 'https://static.mservice.io/blogscontents/momo-upload-api-220422150707-637862368279848663.jpeg','true'
);



SET IDENTITY_INSERT CHARITY OFF

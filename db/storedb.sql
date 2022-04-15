-- 음식점 테이블 생성
create table Restaurants (
	id int unsigned not null auto_increment,
    name varchar(64) not null,
    address varchar(256) not null,
    image_url varchar(512),
    primary key(id)
);

-- 메뉴 테이블 생성
create table Items(
	id int unsigned not null auto_increment,    	
	name varchar(31) not null,
    price int not null default 0,    
    store_id int unsigned not null,
    primary key(id),
    foreign key(store_id) references Restaurants(id)
);

-- 음식점 데이터 입력 방식 
insert into Restaurants (name, address, image_url) values('음식점 이름', '서울', '이미지 주소.jpg');

-- 메뉴 데이터 입력방식
insert into Items (name, price, store_id) values('메뉴 이름', 36000, 1, '이미지 주소');

-- 음식점 삭제시 메뉴도 같이 삭제
alter table Items 
	Add constraint store_id 
	foreign key (store_id)
	references Restaurants(id)  
	ON DELETE CASCADE;
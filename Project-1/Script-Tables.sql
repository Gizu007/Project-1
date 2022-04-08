drop table if exists ERS_User_Roles cascade;
drop table if exists ERS_Reimbursement_Status cascade;
drop table if exists ERS_Reimbursement_Type cascade;
drop table if exists ERS_User cascade;
drop table if exists ERS_Reimbursement cascade;

create table ERS_User_Roles (
	User_Role_Id SERIAL primary key,
	role VARCHAR(50) not null
);

insert into ERS_User_Roles (role)
values ('Finance-Manager'),
('Employee');

create table ERS_User (
	User_Id SERIAL primary key,
	ERS_Username VARCHAR(50) not null,
	ERS_Password VARCHAR(50) not null,
	User_First_Name VARCHAR(100) not null,
	User_Last_Name VARCHAR(100) not null,
	User_Email VARCHAR(150) not null,
	User_Role_Id INTEGER not null,
	
	constraint fk_User_Role_Id foreign key (User_Role_Id) REFERENCES ERS_User_Roles(User_Role_Id) on delete cascade);

create extension pgcrypto;
insert into ERS_User (ERS_Username, ERS_Password, User_First_Name, User_Last_Name, User_Email, User_Role_Id)
values 
('john_smith', crypt('pass123', gen_salt('md5')), 'John', 'Smith', 'john_smith@hotmail.com', 1),
('jane_smith', crypt('pass12', gen_salt('md5')), 'Jane', 'Smith', 'jane_smith@hotmail.com', 2),
('mike_smith', crypt('pass13', gen_salt('md5')), 'Mike', 'Smith', 'mike_smith@hotmail.com', 2);

select *
from ERS_User;
create table ERS_Reimbursement_Status(
	Reimb_Status_Id SERIAL primary key,
	Reimb_Status VARCHAR(10) not null
);

insert into ERS_Reimbursement_Status (Reimb_Status)
values ('Pending'),
('Approved'),
('Denied');

select *
from ERS_Reimbursement_Status;

create table ERS_Reimbursement_Type(
	Reimb_Type_Id SERIAL primary key,
	Reimb_Type VARCHAR(10) not null
);

insert into ERS_Reimbursement_Type (Reimb_Type)
values ('Lodging'),
('Travel'),
('Food'),
('Other');


create table ERS_Reimbursement (
	Reimb_Id SERIAL primary key,
	Reimb_Amount integer not null,
	Reimb_Submitted timestamp not null,
	Reimb_Resolved timestamp,
	Reimb_Description VARCHAR(250) not null,
	Reimb_Receipt text not null,
	Reimb_Author integer not null,
	Reimb_Resolver integer,
	Reimb_Status_Id integer default 1,
	Reimb_Type_Id integer not null,
	constraint fk_Reimb_Author foreign key (Reimb_Author) references ERS_User(User_id) on delete cascade,
	constraint fk_Reimb_Resolver foreign key (Reimb_Resolver) references ERS_User(User_id) on delete cascade,
	constraint fk_Reimb_Status foreign key (Reimb_Status_Id) references ERS_Reimbursement_Status(Reimb_Status_Id) on delete cascade,
	constraint fk_Reimb_Type foreign key (Reimb_Type_Id) references ERS_Reimbursement_Type(Reimb_Type_Id) on delete cascade
);

set timezone to 'EST';

select NOW();
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- O id vem do console admin do keycloak aba "users" criados
-- Copie os ids que foram gerados lá e substitua nos inserts abaixo 
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('fe425bbb-d016-443f-bf53-4a94e73bd364', 'app_user', 'Standard', 'User');
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('99c1da33-0600-4134-bd35-a19a164b4cb7', 'app_admin', 'Admin', 'User');
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('a2cb067e-d691-46b0-b1ce-b9845aedb911', 'app_super_user', 'Super', 'User');

-- O document-id deve ser de um gerado pelo twitter ou simulado através do Postman no request "Elastic Twitter Index Post"
-- inserir no Postman os ids 1, 2 e 3
insert into documents(id, document_id)
values ('c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 1);
insert into documents(id, document_id)
values ('f2b2d644-3a08-4acb-ae07-20569f6f2a01', 2);
insert into documents(id, document_id)
values ('90573d2b-9a5d-409e-bbb6-b94189709a19', 3);

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'fe425bbb-d016-443f-bf53-4a94e73bd364', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'99c1da33-0600-4134-bd35-a19a164b4cb7', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'99c1da33-0600-4134-bd35-a19a164b4cb7', 'f2b2d644-3a08-4acb-ae07-20569f6f2a01', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), '99c1da33-0600-4134-bd35-a19a164b4cb7', '90573d2b-9a5d-409e-bbb6-b94189709a19', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), 'a2cb067e-d691-46b0-b1ce-b9845aedb911', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');



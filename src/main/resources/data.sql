-- users
insert into users (id, first_name, last_name, email, user_role, balance) values (-1, 'ME', 'MYSELF AND I', 'me@testing.com', 'PAL', 0.0);
insert into users (id, first_name, last_name, email, user_role, balance) values (0, 'Will', 'Byers', '2strange4u@testing.com', 'MEMBER', 0.0);
insert into users (id, first_name, last_name, email, user_role, balance) values (1, 'Jim', 'Hopper', 'jimothy@testing.com', 'PAL', 0.0);
insert into users (id, first_name, last_name, email, user_role, balance) values (2, 'Billy', 'Hargrove', 'billiam@testing.com', 'PAL', 0.0);
insert into users (id, first_name, last_name, email, user_role, balance) values (3, 'Steve', 'Harrington', 'steve4eva@testing.com', 'PAL', 0.0);
insert into users (id, first_name, last_name, email, user_role, balance) values (4, 'Lucas', 'Sinclair', '3strange5u@testing.com', 'MEMBER', 0.0);


-- visit requests
insert into visit_requests (id, requestor_id, fulfilled, transaction_id, requested_date, active, tasks_requested) values (1, 1, 0, null, 19381, 1, 'help!');
insert into visit_requests (id, requestor_id, fulfilled, transaction_id, requested_date, active, tasks_requested) values (2, 1, 0, null, 19381, 0, 'help for real though!');
insert into visit_requests (id, requestor_id, fulfilled, transaction_id, requested_date, active, tasks_requested) values (3, 4, 1, null, 19381, 0, 'help!');
insert into visit_requests (id, requestor_id, fulfilled, transaction_id, requested_date, active, tasks_requested) values (4, 5, 0, null, 19381, 1, 'help!');
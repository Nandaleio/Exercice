SELECT 'Hello world';
INSERT INTO RULE (id, name, max_Pins, max_Frames, max_Rolls_Per_Frame, strike_Bonus, strike_After_Pins, spare_Bonus, spare_After_Pins) VALUES (1, 'Classic Bowling', 10, 10, 2, 10, 2, 10, 1);
INSERT INTO RULE (id, name, max_Pins, max_Frames, max_Rolls_Per_Frame, strike_Bonus, strike_After_Pins, spare_Bonus, spare_After_Pins) VALUES (2, 'African Version', 15, 5, 3, 15, 2, 15, 3);

INSERT INTO USER_PLAYER (id, username, password) VALUES (-1, 'test', '$2a$12$K4CuOyl7AP0VDWfBN8mevexQq/NVTwdWgQrcBvAbMyCr.zVm0hq8i') -- password is 'test'



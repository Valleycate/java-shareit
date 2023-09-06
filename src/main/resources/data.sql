CREATE UNIQUE INDEX item_Id_And_Start
ON booking (item_id,start_date);

CREATE UNIQUE INDEX item_Id_And_End
ON booking (item_id,end_date);

CREATE UNIQUE INDEX item_Id
ON comments (item_id);

CREATE INDEX owner_id
ON items (owner_id);
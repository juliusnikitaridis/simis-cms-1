create table cannacomply.farm
(
    id varchar,
    name varchar,
    latitude varchar,
    longitude varchar,
    logo_data varchar,
    type varchar,
    location_data varchar,
    address varchar,
    user_id varchar
);

alter table cannacomply.farm owner to postgres;

create table cannacomply.crop
(
    id varchar,
    crop_type varchar,
    growth_stage varchar,
    status varchar,
    strain_name varchar,
    seed_company varchar,
    farm_id varchar,
    user_id varchar,
    crop_label varchar,
    starting_plant_data varchar,
    created_date varchar,
    barcode_data varchar,
    block_id varchar,
    room_id varchar
);

alter table cannacomply.crop owner to postgres;

create table cannacomply.schedule
(
    id varchar,
    farm_id varchar,
    status varchar,
    starting_date varchar,
    ending_date varchar,
    title varchar,
    type varchar,
    assigned_to varchar,
    description varchar,
    repeat varchar
);

alter table cannacomply.schedule owner to postgres;

create table cannacomply.activity
(
    id varchar,
    crop_id varchar,
    type varchar,
    activity_data varchar,
    user_id varchar,
    farm_id varchar,
    date varchar,
    status varchar,
    block_id varchar,
    item_type varchar,
    item_id varchar
);

alter table cannacomply.activity owner to postgres;

create table cannacomply.treatment_product
(
    id varchar,
    product_name varchar,
    container varchar,
    mass varchar,
    quantity varchar,
    farm_id varchar,
    image_data varchar,
    product_id varchar,
    active_ingredients varchar,
    expiry_date varchar,
    instructions varchar,
    purpose varchar
);

alter table cannacomply.treatment_product owner to postgres;

create table cannacomply.issue
(
    id varchar,
    crop_id varchar,
    title varchar,
    description varchar,
    severity varchar,
    assigned_to varchar,
    comment varchar,
    attachments varchar,
    farm_id varchar
);

alter table cannacomply.issue owner to postgres;

create table cannacomply.block
(
    id varchar,
    block_location varchar,
    barcode_data varchar,
    farm_id varchar,
    date varchar
);

alter table cannacomply.block owner to postgres;

create table cannacomply.room
(
    id varchar,
    room_name varchar,
    farm_id varchar,
    room_description varchar,
    room_color varchar,
    location_data varchar
);

alter table cannacomply.room owner to postgres;

create table cannacomply.reading
(
    id varchar,
    temp varchar,
    ph varchar,
    hummidity varchar,
    room_id varchar,
    date varchar
);

alter table cannacomply.reading owner to postgres;

create table cannacomply.harvest_activity_data
(
    product_id integer,
    crop_id integer,
    block_id integer,
    block_name text,
    strain_id integer,
    strain_name text,
    quantity integer,
    destroyed integer
);

alter table cannacomply.harvest_activity_data owner to postgres;

create table cannacomply.yield
(
    quantity varchar,
    loss varchar,
    notes varchar,
    harvest_batch_id varchar,
    location varchar,
    crop_id varchar,
    strain varchar,
    date varchar,
    id varchar,
    farm_id varchar,
    stage varchar,
    from_block_id varchar,
    wet_weight varchar,
    user_id varchar
);

alter table cannacomply.yield owner to postgres;

create table cannacomply.compliance_user
(
    id varchar,
    sys_unique_user_id varchar,
    farm_id varchar,
    user_role varchar
);

alter table cannacomply.compliance_user owner to postgres;

create table cannacomply.packaging
(
    id varchar,
    package_tag varchar,
    item varchar,
    quantity varchar,
    farm_id varchar,
    location varchar,
    status varchar,
    date varchar,
    harvest_id varchar
);

alter table cannacomply.packaging owner to postgres;


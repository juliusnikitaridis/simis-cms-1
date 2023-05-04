create table cannacomply.farm
(
    id varchar,
    name varchar,
    latitude varchar,
    longitude varchar,
    logo_data varchar,
    type varchar,
    location_data varchar,
    address varchar
);

alter table cannacomply.farm owner to postgres;

create table cannacomply.crop
(
    id varchar,
    crop_type varchar,
    block_location varchar,
    growth_stage varchar,
    status varchar,
    strain_name varchar,
    seed_company varchar,
    farm_id varchar,
    user_id varchar,
    crop_label varchar,
    starting_plant_data varchar,
    created_date varchar
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
    description varchar
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
    status varchar
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
    image_data varchar
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


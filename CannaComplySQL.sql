create table cannacomply.farm
(
    id varchar,
    name varchar,
    latitude varchar,
    longitude varchar,
    logo_data varchar
);



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
    starting_plant_data varchar
);



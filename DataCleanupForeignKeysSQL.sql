alter table cannacomply.activity
    add constraint activity_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;


alter table cannacomply.block
    add constraint block_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.contract
    add constraint contract_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;


alter table cannacomply.crop
    add constraint crop_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.customer
    add constraint customer_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;


alter table cannacomply.device
    add constraint device_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.feedback
    add constraint feedback_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.growth_cycle
    add constraint growth_cycle_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.hygiene
    add constraint hygiene_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.issue
    add constraint issue_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.location
    add constraint location_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.packaging
    add constraint packaging_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.reading
    add constraint reading_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.schedule
    add constraint schedule_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.soil_management
    add constraint soil_management_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.soil_pot
    add constraint soil_pot_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.supplier
    add constraint supplier_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.treatment_product
    add constraint treatment_product_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.user_upload
    add constraint user_upload_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.water_management
    add constraint water_management_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.water_source
    add constraint water_source_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;

alter table cannacomply.yield
    add constraint yield_farm_id_fk
        foreign key (farm_id) references cannacomply.farm
            on delete cascade;








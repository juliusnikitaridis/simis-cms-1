package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class VehicleRepository {

    private static Log LOG = LogFactory.getLog(VehicleRepository.class);
    private static String TABLE_NAME = "carfix.vehicles";
    private static String[] PRIMARY_KEY = new String[]{"vehicle_id"};

    public static Vehicle add(Vehicle record) {
        SqlUtils insertValues = new SqlUtils()
                .add("vehicle_id", UUID.randomUUID().toString())
                .add("vin_number", record.getVinNumber())
                .add("registration_number", record.getReqistration())
                .add("make", record.getMake())
                .add("model", record.getModel())
                .add("year", record.getYear())
                .add("fuel_type", record.getFuelType())
                .add("transmission", record.getTransmission())
                .add("odo_reading", record.getOdoReading())
                .add("engine_code", record.getEngineCode());

        try {
            try (Connection connection = DB.getConnection();
                 AutoStartTransaction a = new AutoStartTransaction(connection);
                 AutoRollback transaction = new AutoRollback(connection)) {
                // In a transaction (use the existing connection)
                record.setVehicleId(DB.insertInto(connection, TABLE_NAME, insertValues, PRIMARY_KEY));
                transaction.commit();
                return record;
            }
        } catch (SQLException se) {
            LOG.error("SQLException: " + se.getMessage());
        }
        LOG.error("An id was not set!");
        return null;
    }



    public static List<Vehicle> findAll(VehicleSpecification specification, DataConstraints constraints) {
        if (constraints == null) {
            constraints = new DataConstraints();
        }
        constraints.setDefaultColumnToSortBy("make");
        DataResult result = query(specification, constraints);
        return (List<Vehicle>) result.getRecords();
    }



    public static Vehicle findById(long id) {
        if (id == -1) {
            return null;
        }
        return (Vehicle) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("vehicle_id = ?", id),
                VehicleRepository::buildRecord);
    }



    private static DataResult query(VehicleSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("vehicle_id = ?", specification.getVehicleId(), -1);

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, VehicleRepository::buildRecord);
    }



    private static Vehicle buildRecord(ResultSet rs) {

        Vehicle vehicle = new Vehicle();
        try {
            vehicle.setVehicleId(rs.getLong("vehicle_id"));
            vehicle.setEngineCode(rs.getString("engine_code"));
            vehicle.setFuelType(rs.getString("fuel_type"));
            vehicle.setMake(rs.getString("make"));
            vehicle.setModel(rs.getString("model"));
            vehicle.setOdoReading(rs.getString("odo_reading"));
            vehicle.setRegistration(rs.getString("registration"));
            vehicle.setTransmission(rs.getString("transmission"));
            vehicle.setVinNumber(rs.getString("vin_number"));
            vehicle.setYear(rs.getString("year"));
            return vehicle;
        } catch (Exception e) {
            LOG.error("exception when building record for vehicle"+e.getMessage());
            return null;
        }
    }
}


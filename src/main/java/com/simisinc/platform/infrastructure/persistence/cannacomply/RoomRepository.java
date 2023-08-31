package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Activity;
import com.simisinc.platform.domain.model.cannacomply.Block;
import com.simisinc.platform.domain.model.cannacomply.Room;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class RoomRepository {

    private static Log LOG = LogFactory.getLog(RoomRepository.class);
    private static String TABLE_NAME = "cannacomply.room";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Room add(Room record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("room_name", record.getRoomName())
                .add("room_description", record.getRoomDescription())
                .add("room_color", record.getRoomColour())
                .add("farm_id",record.getFarmId())
                .add("purpose",record.getPurpose())
                .add("location_data",record.getLocationData());

        try (Connection connection = DB.getConnection();
             AutoStartTransaction a = new AutoStartTransaction(connection);
             AutoRollback transaction = new AutoRollback(connection)) {
            // In a transaction (use the existing connection)
            DB.insertIntoWithStringPk(connection, TABLE_NAME, insertValues, PRIMARY_KEY);
            transaction.commit();
            return record;

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }


    public static void update(Room record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("room_name", record.getRoomName())
                .addIfExists("room_description", record.getRoomDescription())
                .addIfExists("room_color", record.getRoomColour())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("purpose",record.getPurpose())
                .addIfExists("location_data",record.getLocationData());

        try (Connection connection = DB.getConnection();
                 AutoStartTransaction a = new AutoStartTransaction(connection);
                 AutoRollback transaction = new AutoRollback(connection)) {
                // In a transaction (use the existing connection)
                DB.update(connection, TABLE_NAME, updateValues, new SqlUtils().add("id = ?", record.getId()));
                transaction.commit();

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }


    public static Activity findById(String id) {

        return (Activity) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                RoomRepository::buildRecord);
    }


    public static DataResult query(RoomSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("farm_id = ?",specification.getFarmId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, RoomRepository::buildRecord);
    }


    public static void delete(String blockId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", blockId));
            LOG.debug("Room has been deleted:>>" + blockId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Room buildRecord(ResultSet rs) {

        Room room = new Room();
        try {
              room.setId(rs.getString("id"));
              room.setPurpose(rs.getString("purpose"));
              room.setRoomName(rs.getString("room_name"));
              room.setFarmId(rs.getString("farm_id"));
              room.setRoomDescription(rs.getString("room_description"));
              room.setRoomColour(rs.getString("room_color"));
              room.setLocationData(rs.getString("location_data"));

            return room;
        } catch (Exception e) {
            LOG.error("exception when building record for room" + e.getMessage());
            return null;
        }
    }
}


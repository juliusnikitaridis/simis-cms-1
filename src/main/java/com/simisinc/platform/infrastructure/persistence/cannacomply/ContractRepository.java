package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Contract;
import com.simisinc.platform.domain.model.cannacomply.GrowthCycle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class ContractRepository {

    private static Log LOG = LogFactory.getLog(ContractRepository.class);
    private static String TABLE_NAME = "cannacomply.contract";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Contract add(Contract record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("strain",record.getStrain())
                .add("customer_id",record.getCustomerId())
                .add("farm_id",record.getFarmId())
                .add("delivery_date",record.getDeliveryDate())
                .add("type",record.getType())
                .add("price",record.getPrice())
                .add("quantity",record.getQuantity())
                .add("contract_date",record.getContractDate())
                .add("growth_cycle_id",record.getGrowthCycleId());

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


    public static void update(Contract record) throws Exception {
        SqlUtils updateValues = new SqlUtils()

                .addIfExists("strain",record.getStrain())
                .addIfExists("customer_id",record.getCustomerId())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("delivery_date",record.getDeliveryDate())
                .addIfExists("type",record.getType())
                .addIfExists("price",record.getPrice())
                .addIfExists("quantity",record.getQuantity())
                .addIfExists("contract_date",record.getContractDate())
                .addIfExists("growth_cycle_id",record.getGrowthCycleId());
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


    public static GrowthCycle findById(String id) {

        return (GrowthCycle) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                ContractRepository::buildRecord);
    }


    public static DataResult query(ContractSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("farm_id = ?",specification.getFarmId())
                    .addIfExists("customer_id = ?",specification.getCustomerId())
                    .addIfExists("growth_cycle_id = ?",specification.getGrowthCycleId());
        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ContractRepository::buildRecord);
    }


    public static void delete(String issueId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", issueId));
            LOG.debug("growth cycle has been deleted:>>" + issueId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Contract buildRecord(ResultSet rs) {

        Contract contract = new Contract();
        try {
          contract.setId(rs.getString("id"));
          contract.setStrain(rs.getString("strain"));
          contract.setCustomerId(rs.getString("customer_id"));
          contract.setFarmId(rs.getString("farm_id"));
          contract.setDeliveryDate(rs.getString("delivery_date"));
          contract.setType(rs.getString("type"));
          contract.setPrice(rs.getString("price"));
          contract.setQuantity(rs.getString("quantity"));
          contract.setContractDate(rs.getString("contract_date"));
          contract.setGrowthCycleId(rs.getString("growth_cycle_id"));
          return contract;
        } catch (Exception e) {
            LOG.error("exception when building record for contract" + e.getMessage());
            return null;
        }
    }
}


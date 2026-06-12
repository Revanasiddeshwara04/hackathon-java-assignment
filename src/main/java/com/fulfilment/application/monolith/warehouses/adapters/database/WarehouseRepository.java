package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.panache.common.Parameters;
import jakarta.transaction.Transactional;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  private static final Logger LOG =
          Logger.getLogger(WarehouseRepository.class);

  @Override
  public List<Warehouse> getAll() {
    return this.listAll()
            .stream()
            .map(DbWarehouse::toWarehouse)
            .toList();
  }

  @Override
  @Transactional
  public void create(Warehouse warehouse) {

    LOG.info("Creating warehouse: " + warehouse.businessUnitCode);
    DbWarehouse dbWarehouse = new DbWarehouse();
    dbWarehouse.businessUnitCode = warehouse.businessUnitCode;
    dbWarehouse.location = warehouse.location;
    dbWarehouse.capacity = warehouse.capacity;
    dbWarehouse.stock = warehouse.stock;
    dbWarehouse.createdAt = warehouse.createdAt;
    dbWarehouse.archivedAt = warehouse.archivedAt;

    persist(dbWarehouse);
  }

  @Override
  @Transactional
  public void update(Warehouse warehouse) {

    LOG.info("Updating warehouse: " + warehouse.businessUnitCode);

    DbWarehouse dbWarehouse =
            find("businessUnitCode", warehouse.businessUnitCode)
                    .firstResult();

    if (dbWarehouse == null) {
      throw new IllegalArgumentException(
              "Warehouse not found: " + warehouse.businessUnitCode);
    }

    dbWarehouse.location = warehouse.location;
    dbWarehouse.capacity = warehouse.capacity;
    dbWarehouse.stock = warehouse.stock;
    dbWarehouse.archivedAt = warehouse.archivedAt;

    getEntityManager().flush();
  }

  @Override
  @Transactional
  public void remove(Warehouse warehouse) {

    LOG.info("Removing warehouse: " + warehouse.businessUnitCode);

    DbWarehouse dbWarehouse =
            find("businessUnitCode", warehouse.businessUnitCode)
                    .firstResult();

    if (dbWarehouse != null) {
      delete(dbWarehouse);
    }
  }
  @Override
  @Transactional
  public Warehouse findByBusinessUnitCode(String buCode) {

    DbWarehouse dbWarehouse =
            getEntityManager()
                    .createQuery(
                            "SELECT w FROM DbWarehouse w WHERE w.businessUnitCode = :code",
                            DbWarehouse.class)
                    .setParameter("code", buCode)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

    return dbWarehouse != null
            ? dbWarehouse.toWarehouse()
            : null;
  }

    public List<Warehouse> search(
            String location,
            Integer minCapacity,
            Integer maxCapacity) {

        return listAll()
                .stream()
                .filter(w -> w.archivedAt == null)
                .filter(w -> location == null || location.equals(w.location))
                .filter(w -> minCapacity == null || w.capacity >= minCapacity)
                .filter(w -> maxCapacity == null || w.capacity <= maxCapacity)
                .map(DbWarehouse::toWarehouse)
                .toList();
    }
}
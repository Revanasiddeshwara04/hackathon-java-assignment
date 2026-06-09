package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

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

    getEntityManager()
            .createQuery(
                    "UPDATE DbWarehouse w " +
                            "SET w.location = :loc, " +
                            "w.capacity = :cap, " +
                            "w.stock = :stock, " +
                            "w.archivedAt = :archived " +
                            "WHERE w.businessUnitCode = :code")
            .setParameter("loc", warehouse.location)
            .setParameter("cap", warehouse.capacity)
            .setParameter("stock", warehouse.stock)
            .setParameter("archived", warehouse.archivedAt)
            .setParameter("code", warehouse.businessUnitCode)
            .executeUpdate();

    getEntityManager().flush();
    getEntityManager().clear();
  }

  @Override
  public void remove(Warehouse warehouse) {
    throw new UnsupportedOperationException("Unimplemented method 'remove'");
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
}
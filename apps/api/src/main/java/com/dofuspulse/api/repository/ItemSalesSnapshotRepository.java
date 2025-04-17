package com.dofuspulse.api.repository;

import com.dofuspulse.api.model.ItemSalesSnapshot;
import com.dofuspulse.api.projections.ItemPrice;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemSalesSnapshotRepository extends JpaRepository<ItemSalesSnapshot, Long> {

  @Query(value =
      "SELECT DISTINCT ON (s.snapshot_date, s.item_id) s.snapshot_date AS snapshotDate, s.item_id, "
          + " s.prices " + "FROM item_sales_snapshots s " + "WHERE s.item_id IN (:itemIds) "
          + "AND s.snapshot_date BETWEEN :startDate AND :endDate "
          + "ORDER BY s.snapshot_date, s.item_id,s.prices[1]", nativeQuery = true)
  List<ItemPrice> getPriceHistoryInDateRangeForItems(
      @Param("itemIds") List<Long> itemIds,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  List<ItemSalesSnapshot> findAllByItemIdInAndSnapshotDateIsBetween(
      List<Long> itemId,
      LocalDate startDate,
      LocalDate endDate);

  @Query("SELECT DISTINCT " + "iss1.itemId as itemId, " + "iss1.snapshotDate as snapshotDate, "
      + "iss1.prices as prices " + "FROM ItemSalesSnapshot iss1 " + "WHERE iss1.itemId IN :itemIds "
      + "AND iss1.snapshotDate BETWEEN :startDate AND :endDate " + "ORDER BY iss1.snapshotDate")
  List<ItemPrice> getItemsSnapshotsByIdsInDateRange(
      @Param("itemIds") List<Long> itemIds,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

}
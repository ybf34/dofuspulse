package com.dofuspulse.api.repository;

import com.dofuspulse.api.model.ItemMarketEntry;
import com.dofuspulse.api.projections.ItemPrice;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemMarketEntryRepository extends JpaRepository<ItemMarketEntry, Long> {

  @Query(value =
      "SELECT DISTINCT ON (s.entry_date, s.item_id) s.entry_date AS snapshotDate, s.item_id, "
          + " s.prices " + "FROM item_market_entry s " + "WHERE s.item_id IN (:itemIds) "
          + "AND s.entry_date BETWEEN :startDate AND :endDate "
          + "ORDER BY s.entry_date, s.item_id,s.prices[1]", nativeQuery = true)
  List<ItemPrice> getPriceHistoryInDateRangeForItems(
      @Param("itemIds") List<Long> itemIds,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  List<ItemMarketEntry> findAllByItemIdInAndEntryDateIsBetween(
      List<Long> itemId,
      LocalDate startDate,
      LocalDate endDate);

  @Query("SELECT DISTINCT " + "iss1.itemId as itemId, " + "iss1.entryDate as snapshotDate, "
      + "iss1.prices as prices " + "FROM ItemMarketEntry iss1 " + "WHERE iss1.itemId IN :itemIds "
      + "AND iss1.entryDate BETWEEN :startDate AND :endDate " + "ORDER BY iss1.entryDate")
  List<ItemPrice> getItemsSnapshotsByIdsInDateRange(
      @Param("itemIds") List<Long> itemIds,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

}
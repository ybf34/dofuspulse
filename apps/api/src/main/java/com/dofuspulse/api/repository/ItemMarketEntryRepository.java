package com.dofuspulse.api.repository;

import com.dofuspulse.api.model.ItemMarketEntry;
import com.dofuspulse.api.projections.CraftCost;
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

  @Query(value = """
      WITH item_ingredients AS (
          SELECT
              item_id,
              unnest(ingredient_ids) AS ingredient_id,
              unnest(quantities) AS quantity
          FROM item_details
          WHERE item_id IN (:itemIds)
      ),
      ingredient_prices AS (
          SELECT
              s.item_id AS ingredient_id,
              s.entry_date,
              (
                  SELECT MIN(unit_price) FROM (
                      SELECT FLOOR(p.price::float / POWER(10, p.idx - 1)) AS unit_price
                      FROM unnest(s.prices) WITH ORDINALITY AS p(price, idx)
                      WHERE (p.price::float / POWER(10, p.idx - 1)) > 0
                  ) x
              ) AS min_unit_price
          FROM item_market_entry s
          WHERE s.item_id IN (SELECT ingredient_id FROM item_ingredients)
            AND s.entry_date BETWEEN :startDate AND :endDate
      ),
      cost_per_date AS (
          SELECT
              ii.item_id,
              ip.entry_date,
              SUM(ip.min_unit_price * ii.quantity)::int AS craft_cost
          FROM item_ingredients ii
          JOIN ingredient_prices ip
            ON ii.ingredient_id = ip.ingredient_id
          GROUP BY ii.item_id, ip.entry_date
          HAVING COUNT(*) = (SELECT COUNT(*) FROM item_ingredients WHERE item_id = ii.item_id)
      )
      SELECT item_id, entry_date AS snapshotDate, craft_cost AS craftCost FROM cost_per_date
      ORDER BY item_id, entry_date
      """, nativeQuery = true)
  List<CraftCost> getItemCraftCost(
      @Param("itemIds") List<Long> itemIds,
      @Param("startDate") java.time.LocalDate startDate,
      @Param("endDate") java.time.LocalDate endDate
  );
}
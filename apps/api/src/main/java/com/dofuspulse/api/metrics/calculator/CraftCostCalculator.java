package com.dofuspulse.api.metrics.calculator;

import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.CraftCostParams;
import com.dofuspulse.api.metrics.calculator.utils.PriceUtil;
import com.dofuspulse.api.projections.CraftCost;
import com.dofuspulse.api.projections.ItemPrice;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CraftCostCalculator implements MetricCalculator<CraftCostParams, List<CraftCost>> {

  @Override
  public MetricType getType() {
    return MetricType.CRAFT_COST;
  }

  @Override
  public Class<CraftCostParams> getInputType() {
    return CraftCostParams.class;
  }

  @Override
  public List<CraftCost> calculate(CraftCostParams data) {

    Map<Long, Map<LocalDate, ItemPrice>> priceMap = data.ingredientsPrices().stream().collect(
        Collectors.groupingBy(ItemPrice::getItemId,
            Collectors.toMap(ItemPrice::getSnapshotDate, price -> price)));

    Set<LocalDate> dates = data.ingredientsPrices().stream().map(ItemPrice::getSnapshotDate)
        .collect(Collectors.toCollection(TreeSet::new));

    List<Long> ingredientIds = data.ingredientQuantityMap().keySet().stream().toList();

    return dates.stream().map(date -> {
      List<Optional<Integer>> ingredientsCost = ingredientIds.stream().map(ingredientId -> {

        ItemPrice itemPrice = priceMap.getOrDefault(ingredientId, Collections.emptyMap()).get(date);

        if (itemPrice == null) {
          return Optional.<Integer>empty();
        }

        int price = PriceUtil.getMinimumUnitPrice(itemPrice.getPrices());
        int quantity = data.ingredientQuantityMap().get(ingredientId);

        return price != 0 ? Optional.of(price * quantity) : Optional.<Integer>empty();

      }).toList();

      Optional<Integer> totalCost =
          ingredientsCost.stream().anyMatch(Optional::isEmpty) ? Optional.empty()
              : Optional.of(ingredientsCost.stream().mapToInt(c -> c.orElse(0)).sum());

      return totalCost.map(craftCost -> new CraftCost(date, craftCost));

    }).flatMap(Optional::stream).collect(Collectors.toList());
  }
}

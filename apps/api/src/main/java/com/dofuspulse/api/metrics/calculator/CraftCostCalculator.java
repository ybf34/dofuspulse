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
import java.util.Objects;
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
      List<Integer> craftCost = ingredientIds.stream().mapToInt(ingredientId -> {

        ItemPrice priceForDate = priceMap.getOrDefault(ingredientId, Collections.emptyMap())
            .get(date);

        return (priceForDate != null) ? PriceUtil.getMinimumUnitPrice(priceForDate.getPrices())
            * data.ingredientQuantityMap().get(ingredientId) : 0;
      }).boxed().toList();

      return craftCost.stream().anyMatch(cost -> cost == 0) ? null
          : new CraftCost(date, craftCost.stream().mapToInt(Integer::intValue).sum());

    }).filter(Objects::nonNull).collect(Collectors.toList());
  }
}

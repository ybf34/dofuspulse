package com.dofuspulse.api.projections;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemMarketEntryProjection {

  private Long itemId;
  private LocalDate entryDate;
  private List<Integer> prices;
  private Map<String, String> effects;

}
package com.dofuspulse.api.projections;

import java.time.LocalDate;
import java.util.List;

public interface ItemPrice {

  Long getItemId();

  LocalDate getSnapshotDate();

  List<Integer> getPrices();
}

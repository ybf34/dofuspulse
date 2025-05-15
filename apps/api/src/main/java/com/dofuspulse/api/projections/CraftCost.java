package com.dofuspulse.api.projections;

import java.time.LocalDate;

/**
 * Projection interface for CraftCost.
 */
public interface CraftCost {

  Long getItemId();

  LocalDate getSnapshotDate();

  int getCraftCost();
}
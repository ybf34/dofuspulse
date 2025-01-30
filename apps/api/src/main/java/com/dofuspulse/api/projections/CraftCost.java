package com.dofuspulse.api.projections;

import java.time.LocalDate;


public record CraftCost(LocalDate snapshotDate, int craftCost) {}
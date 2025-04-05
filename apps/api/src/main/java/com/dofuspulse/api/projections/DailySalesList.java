package com.dofuspulse.api.projections;

import java.util.List;

public record DailySalesList(Long itemId, List<DailySales> dailySales) {
}
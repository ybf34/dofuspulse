package com.dofuspulse.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(ItemMarketEntryId.class)
@Table(name = "item_market_entry")
public class ItemMarketEntry {

  @Id
  @Column(name = "entry_id", columnDefinition = "uuid")
  private UUID id;

  @Column(name = "item_id")
  private Long itemId;

  @Id
  @Column(name = "entry_date")
  private LocalDate entryDate;

  @Column(columnDefinition = "jsonb")
  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, String> effects;

  @Column(name = "prices", columnDefinition = "integer[]")
  private List<Integer> prices;

}

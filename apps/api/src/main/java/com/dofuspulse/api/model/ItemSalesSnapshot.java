package com.dofuspulse.api.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item_sales_snapshots")
public class ItemSalesSnapshot {

  @Id
  @GeneratedValue(generator = "snapshot_id_seq")
  @SequenceGenerator(name = "snapshot_id_seq", allocationSize = 1)
  @Column(name = "snapshot_id")
  private Long id;

  @Column(name = "item_id")
  private Long itemId;

  @Column(name = "snapshot_date")
  private LocalDate snapshotDate;

  @Type(JsonBinaryType.class)
  @Column(columnDefinition = "jsonb")
  private Map<String, String> effects;

  @Type(ListArrayType.class)
  @Column(name = "prices", columnDefinition = "integer[]")
  private List<Integer> prices;

}

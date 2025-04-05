package com.dofuspulse.api.items.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.items.controller.ItemTypeController;
import com.dofuspulse.api.items.dto.ItemTypeDto;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.items.service.ItemTypeServiceImpl;
import com.dofuspulse.api.security.CustomAccessDeniedHandler;
import com.dofuspulse.api.security.UnauthorizedHandler;
import com.dofuspulse.api.security.WebSecurityConfig;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(ItemTypeController.class)
@WithMockUser
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
public class ItemTypeControllerUnitTest {

  @Autowired
  MockMvcTester mockMvcTester;

  @MockitoBean
  ItemTypeServiceImpl itemTypeService;

  @Test
  void shouldReturnItemTypeByIdWith200Status() {
    ItemTypeDto mockItemType = new ItemTypeDto(ItemTestDataFactory.createMockItemType());

    when(itemTypeService.getItemTypeById(1L)).thenReturn(Optional.of(mockItemType));

    mockMvcTester.get()
        .uri("/api/v1/item-types/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .hasPath("$.id")
        .hasPath("$.name");
  }

  @Test
  void shouldReturnItemTypesPageWith200Status() {

    Pageable pageable = Pageable.ofSize(20);
    ItemTypeDto itemType = new ItemTypeDto(ItemTestDataFactory.createMockItemType());
    Page<ItemTypeDto> mockItemTypePage = new PageImpl<>(List.of(itemType), pageable, 1);

    when(itemTypeService.getItemTypes(any(Pageable.class))).thenReturn(mockItemTypePage);

    mockMvcTester.get()
        .uri("/api/v1/item-types")
        .contentType(MediaType.APPLICATION_JSON)
        .queryParam("size", "20")
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .hasPath("$.page.totalElements")
        .hasPath("$.page.totalPages")
        .hasPath("$.page.size")
        .hasPath("$.content");

    verify(itemTypeService, times(1)).getItemTypes(any(Pageable.class));
  }

}

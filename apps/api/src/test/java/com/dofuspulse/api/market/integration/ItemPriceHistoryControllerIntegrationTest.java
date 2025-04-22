package com.dofuspulse.api.market.integration;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class ItemPriceHistoryControllerIntegrationTest extends PostgresIntegrationTestContainer {


}

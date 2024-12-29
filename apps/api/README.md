

## Dofus Pulse API

### Sales Analytics

#### Get Sales History for a Specific Item

**Endpoint:**
```http
GET /api/v1/items/:itemId/saleshistory
```

**Description:**  
Retrieves the sales history for a specific item.

**Path Parameters:**

| Parameter | Type     | Description                                      |
| :-------- | :------- | :----------------------------------------------- |
| `itemId`  | `string` | **Required**. ID of the item to retrieve history for |

**Query Parameters:**

| Parameter   | Type     | Description                                      |
| :---------- | :------- | :----------------------------------------------- |
| `startDate` | `string` | **Required**. Start date for the sales history in `YYYY-MM-DD` format. |
| `endDate`   | `string` | **Required**. End date for the sales history in `YYYY-MM-DD` format. |

---

#### Get Sales History for Items of a Specific Type

**Endpoint:**
```http
GET /api/v1/type/:typeId/saleshistory
```

**Description:**  
Retrieves the sales history for items of a specific type.

**Path Parameters:**

| Parameter | Type     | Description                                      |
| :-------- | :------- | :----------------------------------------------- |
| `typeId`  | `string` | **Required**. ID of the item type to retrieve history for |

**Query Parameters:**

| Parameter   | Type     | Description                                      |
| :---------- | :------- | :----------------------------------------------- |
| `startDate` | `string` | **Required**. Start date for the sales history in `YYYY-MM-DD` format. |
| `endDate`   | `string` | **Required**. End date for the sales history in `YYYY-MM-DD` format. |

---

### Profit Margin Analytics

#### Get Profit Margin History for a Specific Item

**Endpoint:**
```http
GET /api/v1/items/:itemId/profitmargin
```

**Description:**  
Retrieves the profit margin history for a specific item.

**Path Parameters:**

| Parameter | Type     | Description                                      |
| :-------- | :------- | :----------------------------------------------- |
| `itemId`  | `string` | **Required**. ID of the item to retrieve history for |

**Query Parameters:**

| Parameter   | Type     | Description                                      |
| :---------- | :------- | :----------------------------------------------- |
| `startDate` | `string` | **Required**. Start date for the profit margin history in `YYYY-MM-DD` format. |
| `endDate`   | `string` | **Required**. End date for the profit margin history in `YYYY-MM-DD` format. |

---

#### Get Profit Margin History for Items of a Specific Type

**Endpoint:**
```http
GET /api/v1/type/:typeId/profitmargin
```

**Description:**  
Retrieves the profit margin history for items of a specific type.

**Path Parameters:**

| Parameter | Type     | Description                                      |
| :-------- | :------- | :----------------------------------------------- |
| `typeId`  | `string` | **Required**. ID of the item type to retrieve history for |

**Query Parameters:**

| Parameter   | Type     | Description                                      |
| :---------- | :------- | :----------------------------------------------- |
| `startDate` | `string` | **Required**. Start date for the profit margin history in `YYYY-MM-DD` format. |
| `endDate`   | `string` | **Required**. End date for the profit margin history in `YYYY-MM-DD` format. |

---
### Authentication

#### User Login

**Endpoint:**
```http
POST /api/v1/auth/login
```

**Description:**  
Authenticates a user with their email and password. (Session auth)

**Request Body:**

| Field        | Type     | Description                          |
| :----------- | :------- | :----------------------------------- |
| `email`      | `string` | **Required**. The user's email.      |
| `password`   | `string` | **Required**. The user's password.   |

---

#### User Registration

**Endpoint:**
```http
POST /api/v1/auth/register
```

**Description:**  
Registers a new user with their email, password, and default role as `USER`.

**Request Body:**

| Field        | Type     | Description                          |
| :----------- | :------- | :----------------------------------- |
| `email`      | `string` | **Required**. The user's email.      |
| `password`   | `string` | **Required**. The user's password.   |

--- 

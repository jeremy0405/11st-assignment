# 11st-assignment

## 설계

### **테이블 설계**

<img width="669" alt="image" src="https://user-images.githubusercontent.com/81368630/185779893-952bce70-38ab-4d8b-a459-6a07a8ea3d97.png">

<br>

### **API 설계**

| 기능 | API |
|--|--|
| 상품 조회 | GET /api/products |
| 상품 주문 | POST /api/orders |
| 상품 주문 취소 | POST /api/orders/{order_id}/cancel |
| 주문 내역 조회 | GET /api/orders |


<br>

### **에러 설계**

| 에러 코드 | 내용 |
|--|--|
| A001 | 유효하지 않은 ProductId 요청 시 발생하는 에러 |
| A002 | 유효하지 않은 OrderId 요청 시 발생하는 에러 |
| B001 | 상품 주문 시 상품의 재고가 부족하면 발생하는 에러 |
| B002 | 상품 주문 시 요청의 금액이 상품의 금액보다 작으면 발생하는 에러 |
| B003 | 상품 주문 시 상품의 상태가 판매 중지된 상품이면 발생하는 에러 |
| C001 | 상품 취소 시 주문이 이미 완료된 상태일 때 발생하는 에러 |
| C002 | 상품 취소 시 주문이 이미 취소된 상태일 때 발생하는 에러 |
| C003 | 상품 취소 시 취소 금액과 총 주문 금액이 일치하지 않을 때 발생하는 에러 |
| D001 | 입력 파라미터가 잘못된 경우 발생하는 에러 |

<br>

### **API 요청 및 응답 예시**

<details>
<summary><b> ◀️ Click! - 상품 조회 요청 및 응답 예시 </b></summary>
<div markdown="1">

<br>

**기능**

- display_date 기준으로 전시중인 상품을 페이징 처리하여 반환한다.

**API**

- `GET /api/products`

**Query Param**

- Required
    - None
- Option
    - display_date
        - display_date=2022-08-21T00:00
        - 미입력시 default: 현재시간
    - page
        - page=1
        - 미입력시 default: page=0

**Error Response**

- display_date의 ISO Date Time Format이 잘못되었을 때
- `GET http://localhost:8080/api/products?display_date=2022-08-21T00&page=0`

- Http Status: 400
```json
{
    "errorResponse": {
        "code": "D001",
        "message": "요청이 올바르지 않습니다"
    },
    "errors": [
        {
            "field": "display_date",
            "value": "2022-08-21T00",
            "reason": "typeMismatch"
        }
    ]
}
```

**Success Response**

- `GET http://localhost:8080/api/products?display_date=2022-08-21T00:00&page=1`
- Http Status: 200
```json
{
	"content": [
		{
			"id": 6,
			"name": "(아마존)Corsai 벤전스LPX DDR4 데스크톱 메모리 키트 16GB (2x8GB) 블랙(CMK16GX4M2B3200C16)",
			"price": 84270,
			"quantity": 30,
			"sellerId": 3,
			"sellerName": "하이닉스",
			"status": "SALE"
		},
		{
			"id": 7,
			"name": "갤럭시S22",
			"price": 1200000,
			"quantity": 30,
			"sellerId": 4,
			"sellerName": "삼성",
			"status": "SALE"
		},
		{
			"id": 8,
			"name": "갤럭시 워치 4",
			"price": 220000,
			"quantity": 60,
			"sellerId": 4,
			"sellerName": "삼성",
			"status": "SALE"
		},
		{
			"id": 9,
			"name": "갤럭시S10",
			"price": 1000000,
			"quantity": 100,
			"sellerId": 4,
			"sellerName": "삼성",
			"status": "SUSPENDED"
		},
		{
			"id": 10,
			"name": "갤럭시 버즈 프로",
			"price": 330000,
			"quantity": 100,
			"sellerId": 4,
			"sellerName": "삼성",
			"status": "SALE"
		}
	],
	"pageable": {
		"sort": {
			"sorted": false,
			"unsorted": true,
			"empty": true
		},
		"pageNumber": 1,
		"pageSize": 5,
		"offset": 5,
		"paged": true,
		"unpaged": false
	},
	"totalPages": 2,
	"totalElements": 10,
	"last": true,
	"numberOfElements": 5,
	"sort": {
		"sorted": false,
		"unsorted": true,
		"empty": true
	},
	"size": 5,
	"number": 1,
	"first": false,
	"empty": false
}
```

**특이 사항**
- 캐시 적용
- spring.jpa.default_batch_fetch_size를 통한 N+1 쿼리 해결

</div>
</details>

<br>

<details>
<summary><b> ◀️ Click! - 상품 주문 요청 및 응답 예시 </b></summary>
<div markdown="1">

<br>

**기능**

- 사용자가 상품을 주문하면 주문 수량만큼 상품의 재고가 감소하고 상품이 주문된다.
- 여러 가지 상품을 한 번의 주문에 주문할 수 있다.

**API**

- `POST /api/orders`

**Header**

- `x-user-id:greatpeople`

**Request Body**

```json
{
	"orders": [
		{
			"productId": 2,
			"price": 800000,
			"quantity": 1
		},
		{
			"productId": 4,
			"price": 110000,
			"quantity": 1
		}
	],
	"address": {
		"city": "서울시 송파구",
		"street": "송파대로 567",
		"zipCode": "05503"
	}
}
```

**Error Response**

- Request Body의 productId에 해당하는 상품이 없을 때
- Http Status: 400
```json
{
	"code": "A001",
	"message": "해당 상품이 존재하지 않습니다"
}
```

- Request Body의 수량보다 상품의 재고가 적을 때
- Http Status: 400
```json
{
	"code": "B001",
	"message": "재고가 부족합니다"
}
```

- Request Body의 금액보다 상품의 가격이 클 때
- Http Status: 400
```json
{
	"code": "B002",
	"message": "입금된 금액이 충분하지 않습니다"
}
```

- Request Body의 productId에 해당하는 상품이 판매 중지일 때
- Http Status: 400
```json
{
	"code": "B003",
	"message": "판매 중지된 상품입니다"
}
```

- Request Body의 값이 음수이거나 비어 있어 Valid에서 검증되는 경우
- Http Status: 400
```json
{
	"errorResponse": {
		"code": "D001",
		"message": "요청이 올바르지 않습니다"
	},
	"errors": [
		{
			"field": "orders[1].productId",
			"value": "-9",
			"reason": "0보다 커야 합니다"
		},
		{
			"field": "address.city",
			"value": "",
			"reason": "비어 있을 수 없습니다"
		},
		{
			"field": "orders[1].price",
			"value": "-11000000",
			"reason": "0보다 커야 합니다"
		},
		{
			"field": "orders[1].quantity",
			"value": "-1",
			"reason": "0보다 커야 합니다"
		}
	]
}
```

**Success Response**

- 생성된 주문의 식별값을 반환
- Http Status: 201
```json
{
	"orderId": 8
}
```

**특이 사항**
- 비관적 락을 통해 상품의 수량을 감소시켜 동시성 문제 해결
- Entity에 비즈니스 로직을 넣어 Entity가 직접 자신의 정보를 수정하도록 함

</div>
</details>

<br>

<details>
<summary><b> ◀️ Click! - 상품 주문 취소 요청 및 응답 예시 </b></summary>
<div markdown="1">

<br>

**기능**

- 사용자가 상품 주문 취소하면 상품의 재고가 원래대로 돌아가고 주문이 취소된다.

**API**

- `POST /api/orders/{order_id}/cancel`

**Request Body**

```json
{
	"cancelPrice": 4040000
}
```

**Error Response**

- PathVariable의 orderId에 해당하는 주문이 없을 때
- Http Status: 400
```json
{
	"code": "A002",
	"message": "해당 주문이 존재하지 않습니다"
}
```

- PathVariable의 orderId에 해당하는 주문이 완료된 상태일 때
- Http Status: 400
```json
{
	"code": "C001",
	"message": "이미 완료된 주문은 취소가 불가능합니다"
}
```

- PathVariable의 orderId에 해당하는 주문이 이미 취소 상태일 때
- Http Status: 400
```json
{
	"code": "C002",
	"message": "이미 취소된 주문은 취소가 불가능합니다"
}
```

- Request Body의 취소 금액과 총 주문 금액이 일치하지 않을 때
- Http Status: 400
```json
{
	"code": "C003",
	"message": "취소 금액과 총 주문 금액이 일치하지 않습니다."
}
```

- Request Body의 값이 음수이거나 비어 있어 Valid에서 검증되는 경우
- Http Status: 400
```json
{
  "errorResponse": {
    "code": "D001",
    "message": "요청이 올바르지 않습니다"
  },
  "errors": [
    {
      "field": "cancelPrice",
      "value": "-4040000",
      "reason": "0 이상이어야 합니다"
    }
  ]
}
```

**Success Response**

- 취소된 주문의 식별값을 반환
- Http Status: 200
```json
{
	"orderId": 1
}
```

**특이 사항**

- Entity에 비즈니스 로직을 넣어 Entity가 직접 자신의 정보를 수정하도록 함

</div>
</details>

<br>

<details>
<summary><b> ◀️ Click! - 주문 내역 조회 요청 및 응답 예시 </b></summary>
<div markdown="1">

<br>

**기능**

- start_date, end_date 사이에 해당하는 회원의 주문 내역을 페이징 처리하여 반환한다.

**API**

- `GET /api/products`

**Header**

- `x-user-id:greatpeople`

**Query Param**

- Required
    - start_date
    - end_date
    
**Error Response**

- start_date 또는 end_date의 ISO Date Time Format이 잘못되었을 때
- `GET http://localhost:8080/api/orders?start_date=2022-06-20&end_date=2022-08-21T00:00`
- Http Status: 400
```json
{
  "errorResponse": {
    "code": "D001",
    "message": "요청이 올바르지 않습니다"
  },
  "errors": [
    {
      "field": "start_date",
      "value": "2022-06-20",
      "reason": "typeMismatch"
    }
  ]
}
```

- start_date가 end_date보다 이후의 Date Time 일 때
- `GET http://localhost:8080/api/orders?start_date=9999-12-20&end_date=2022-08-21T00:00`
- Http Status: 400
```json
{
  "code": "D001",
  "message": "요청이 올바르지 않습니다"
}
```


**Success Response**

- `GET http://localhost:8080/api/orders?start_date=2022-06-20T00:00&end_date=2022-08-21T00:00`
- Http Status: 200
```json
{
  "content": [
    {
      "orderId": 2,
      "orderHistories": [
        {
          "productName": "문화상품권",
          "productPrice": 50000,
          "orderPrice": 500000,
          "orderQuantity": 10
        }
      ],
      "address": {
        "city": "서울시 송파구",
        "street": "송파대로 567",
        "zipCode": "05503"
      }
    },
    {
      "orderId": 3,
      "orderHistories": [
        {
          "productName": "(아마존)Corsai 벤전스LPX DDR4 데스크톱 메모리 키트 16GB (2x8GB) 블랙(CMK16GX4M2B3200C16)",
          "productPrice": 84270,
          "orderPrice": 84270,
          "orderQuantity": 1
        }
      ],
      "address": {
        "city": "서울시 송파구",
        "street": "송파대로 567",
        "zipCode": "05503"
      }
    },
    {
      "orderId": 4,
      "orderHistories": [
        {
          "productName": "아이패드",
          "productPrice": 800000,
          "orderPrice": 800000,
          "orderQuantity": 1
        },
        {
          "productName": "애플팬슬",
          "productPrice": 110000,
          "orderPrice": 110000,
          "orderQuantity": 1
        }
      ],
      "address": {
        "city": "서울시 송파구",
        "street": "송파대로 567",
        "zipCode": "05503"
      }
    },
    {
      "orderId": 5,
      "orderHistories": [
        {
          "productName": "갤럭시 버즈 프로",
          "productPrice": 330000,
          "orderPrice": 330000,
          "orderQuantity": 1
        }
      ],
      "address": {
        "city": "서울시 송파구",
        "street": "송파대로 567",
        "zipCode": "05503"
      }
    },
    {
      "orderId": 6,
      "orderHistories": [
        {
          "productName": "맥북프로",
          "productPrice": 3400000,
          "orderPrice": 3400000,
          "orderQuantity": 1
        }
      ],
      "address": {
        "city": "서울시 송파구",
        "street": "송파대로 567",
        "zipCode": "05503"
      }
    }
  ],
  "pageable": {
    "sort": {
      "unsorted": true,
      "sorted": false,
      "empty": true
    },
    "pageNumber": 0,
    "pageSize": 5,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 5,
  "last": true,
  "numberOfElements": 5,
  "number": 0,
  "first": true,
  "size": 5,
  "sort": {
    "unsorted": true,
    "sorted": false,
    "empty": true
  },
  "empty": false
}
```

**특이 사항**

- spring.jpa.default_batch_fetch_size를 통한 N+1 쿼리 해결 

</div>
</details>


<br>


## 빌드 및 실행 방법

```text
git clone https://github.com/jeremy0405/11st-assignment.git

cd 11st-assignment

chmod +x gradlew
./gradlew build

java -jar build/libs/elevenstreet-0.0.1-SNAPSHOT.jar
```

<br>

### h2 console 실행

http://localhost:8080/h2-console

<img width="456" alt="image" src="https://user-images.githubusercontent.com/81368630/185780913-de202727-c16d-4dfd-93a9-4ff29fa5bebf.png">

위와 같이 설정 후 Connect 하여 DB 확인 가능

<br>

## 과제를 구현하며 고민한 사항

### 1. 일관된 에러를 설계하고 에러 응답을 내리자
   - 클라이언트에서 예외 처리 로직을 일관되게 구현할 수 있도록 ErrorResponse를 내리고자 함
   - 하지만 Valid를 통한 에러 발생 시 조금 더 정확한 정보를 반환하고 싶어서 ErrorDetailResponse를 두어 일관된 에러를 내리지 못하고 있음
   - ErrorDetailResponse의 errors를 빈 리스트로 반환하더라도 하나로 통일하는 것이 바람직해 보임

<br>

### 2. 조회 API의 경우 데이터의 양이 방대할 것으로 예상되므로 Page 처리를 하자
   - 조회의 경우 수많은 데이터가 있을 것으로 예상되어 페이징 처리를 함
   - 페이징 처리 시 fetch join을 사용하면 모든 데이터를 가져온 후 어플리케이션 레벨에서 페이징 하기 때문에 성능 저하가 발생함
   - inner join 후 spring.jpa.default_batch_fetch_size 를 통해 해결
   - 상품 조회 시 cache를 적용함
     - 데이터 정합성을 위해서 1분마다 캐시를 삭제하도록 함

<br>

### 3. 비즈니스 로직을 엔티티에서 처리하도록 하자
   - 비즈니스 로직을 엔티티에서 처리하도록 해서 객체지향적인 코드를 작성하고자 노력함
   - 서비스 레이어에서는 Transaction, Cache 적용과 같은 infra 적용 담당

<br>

### 4. 대량 트래픽에서 이슈가 없도록 구현하자
   - 상품 조회 시 캐시를 사용하여 대량 트래픽에서 효율적으로 응답을 반환하도록 함
   - 여러 사용자가 동시 주문 시 동시성 문제를 pessimistic lock으로 해결
     - 일반적인 경우에는 성능이 좋지 않지만 굉장히 많은 트래픽이 들어왔을 때는 성능이 좋음
     - Redis로 Lettuce 또는 Redisson을 적용하는 것이 좋아 보임 

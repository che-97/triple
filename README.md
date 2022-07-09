# 트리플여행자 클럽 마일리지 서비스
<img src="https://img.shields.io/badge/Language-Java 18-green?style=flat"/> <img src="https://img.shields.io/badge/Framework-Spring Boot 2.7.1-blue?style=flat"/> <img src="https://img.shields.io/badge/Build Tool-Gradl-sky?style=flat"/> <img src="https://img.shields.io/badge/DB-MySQL 5.7-yellow?style=flat"/>

<img src="https://img.shields.io/badge/JPA-red"/> <img src="https://img.shields.io/badge/Lombok-red"/> <img src="https://img.shields.io/badge/Junit5-red"/>

### Description

트리플 사용자들이 장소에 리뷰를 작성할 때 포인트를 부여하고, 전체/개인에 대한 포인트 부여 히스토리와 개인별 누적 포인트를 관리하고자 합니다.


### Usage

----
리뷰를 작성하거나 수정, 삭제할때마다 이벤트가 발생합니다.

* **URL**

  /events


* **Method:**

  `POST`


* **URL Params**

   None


* **Data Params**

  |Parameter| Type | Description   |
  |---|--- | ---|    
  |type|String|미리 정의된 string 값을 가지고 있습니다. 리뷰 이벤트의 경우 "REVIEW"로 옵니다.|
  |action|String|리뷰 생성 이벤트의 경우 "ADD" , 수정 이벤트는 "MOD" , 삭제 이벤트는 "DELETE" 값을 가지고 있습니다.|
  |reviewId|String| UUID 포맷의 review id입니다. 어떤 리뷰에 대한 이벤트인지 가리키는 값입니다.|
  |attachedPhotoIds|String[]| 리뷰에 첨부된 이미지들의 id 배열입니다. |
  |userId|String| UUID 포맷의 리뷰 작성자 id 입니다. |
  |placeId|String| UUID 포맷의 리뷰가 작성된 장소의 id입니다.|


* **Sample Params:**

  ```
  {
    "type": "REVIEW",
    "action": "ADD", 
    "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
    "content": "좋아요!",
    "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
    }
  ```

* **Success Response:**

    * **sucess:** true <br />
   **errorCode:** 0 <br />
   **message:** "OK" <br />
   **data:** null <br /><br />
  
    

* **Error Response:**

  * **sucess:** false <br />
    **errorCode:** `errorCode` <br />
    **message:** `error Message` <br /><br />
  * **Sample Response**
     ```
      {
        "success": false,
        "errorCode": 10001,
        "message": "undefined action"
      }
     ```
----

사용자별로 포인트 조회할 수있습니다.

* **URL**

  /user/`:userId`/points


* **Method:**

  `GET`


* **URL Params**

   `userId=[String]`


* **Data Params**

  None


* **Success Response:**

  * **sucess:** true <br />
    **errorCode:** 0 <br />
    **message:** "OK" <br />
    **data:** 사용자별 point 이력 및 총 point를 확인할수 있습니다. <br />
    * id : 사용자 id
    * totalPoint : 누적 Point 
    * pointHistoryList : Point 이력
      * reviewCreateDate : 리뷰 생성일
      * reviewUpdateDate : 리뷰 수정일
      * reviewDeleteYn : 리뷰 삭제 여부 (Y:삭제)
      * createDate : Point 등록일
      * updateDate : Point 삭제일
      * type : Point type
      * message : Point message
      * deleteYn : Point 삭제 여부 (Y:삭제) <br /><br />
  * **Sample Response**
    ```
     {
       "success": true,
       "errorCode": 0,
       "message": "Ok",
        "data": {
           "id": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
           "totalPoint": 5,
           "pointHistoryList": [
               {
                   "reviewCreateDate": "2022-07-09 22:07:10",
                   "reviewUpdateDate": "2022-07-09 22:07:10",
                   "reviewDeleteYn": "N",
                   "createDate": "2022-07-09 22:07:10",
                   "updateDate": "2022-07-09 22:07:10",
                   "type": "REVIEW_PHOTO",
                   "message": "사진 첨부",
                   "deleteYn": "N"
               }
           ]
        }
     }
    ```
    
* **Error Response:**

  * **sucess:** false <br />
    **errorCode:** `errorCode` <br />
    **message:** `error Message` <br /><br />

  * **Sample Response**
    ```
     {
        "success": false,
        "errorCode": 20000,
        "message": "Invalid UUID string: 3ede0ef2-92b7-4817"
     }
    ```
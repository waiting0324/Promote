# RuoYi 架構說明手冊

作者: 6550 劉威廷



## 一、系統需求

JDK 1.8

MySQL >= 5.5

Maven >= 3.0

Redis >= 3.0

Node >= 10



## 二、選用技術框架

- Spring Boot 2.1
- Spring Framework 5.1
- Spring Security 5.1
- Apache MyBatis 3.4
- Alibaba Druid 1.1



## 三、主要功能列表

* 使用者管理：使用者是系統操作者，該功能主要完成系統使用者配置。
* 部門管理：配置系統組織機構（公司，部門，小組），樹結構展現支持數據權限。
* 職位管理：配置系統用戶所屬職位。
* 選單管理：配置系統選單，操作權限，按鈕權限標識等。
* 角色管理：角色選單權限分配，設置角色按機構進行數據范圍權限劃分。
* 字典管理：對系統中經常使用的一些多個固定的數據進行維護。
* 參數管理：對系統動態配置常用參數。
* 通知公告：系統通知公告信息發布維護。
* 操作日誌：系統正常操作日誌記錄和查詢；系統異常信息日誌記錄和查詢。
* 登入日誌：系統登錄日誌記錄查詢包含登入異常。
* 在線使用者：當前系統中活躍使用者狀態監控。
* 定時任務：在線（新增，修改，刪除）任務調度包含執行結果日誌。
* 代碼生成：前後端代碼的生成（java，html，xml，sql）支持CRUD下載。
* 系統接口：根據業務代碼自動生成相關的api接口文檔。
* 服務監控：監視當前系統CPU，內存，磁盤，其他等相關信息。
* 在線整合器：預先表單元素生成相應的HTML代碼。
* 連接池監視：監視當期系統數據庫連接池狀態，可進行分析SQL查找系統性能狀況。



## 四、技術框架介紹



### 1. Spring 

Spring 是 **分層(註1)** 的 **full-stack(註2)** **輕量級(註3)** 開源框架，以 **IoC(註4)** 和 **AOP(註5)** 為內核，提供了展現層 SpringMVC 和業務層事務管理等眾多的企業級應⽤技術，還能整合開源世界眾多著名的第三⽅框架和類庫，已經成為使⽤最多的 Java EE 企業應⽤開源框架。



**名詞解釋**

1. 分層 : Spring可以應用在Web、Service、Dao層中的任何一層。

2. full-stack :  全棧，只用Spring就可以搞定Web應用開發了。

3. 輕量級 : 不需要依賴第三方的軟件，運行時所需資源少。

4. IoC : Inversion of Control (控制反轉) ，將Java對象實例化及管理的權力，交給第三方(此處指Spring)

5. AOP : Aspect oriented Programming ⾯向切⾯編程，是一種橫向抽取技術，在特定方法 執行前或執行後，運行我們要求的代碼。應用場景較為固定，通常用於權限校驗、日誌、事務控制、性能監控。

   註: OOP的繼承，屬於縱向抽取技術。



#### Spring 的優勢

* **方便解耦，簡化開發**

  通過Spring提供的IoC容器，可以將對象間的依賴關係交由Spring進行控制，避免硬編碼所造成的過度程序耦合。

  

* **AOP編程的支持**

  通過Spring的AOP功能，方便進行面向切面的編程，許多不容易用傳統OOP實現的功能可以通過AOP輕鬆應付。

  

* **聲明式事務的支持** @Transactional

  從單調煩悶的事務管理代碼中解脫出來，通過聲明式方式靈活的進行事務的管理，提高開發效率和質量。

  **註: 使用時需注意事務失效的場景**

  

* **⽅便程序的測試**

  替換JUnit的默認運行器，在測試時可以使用@Autoweird注入IOC容器中的對象，不需要每次都去啟動容器。

  

* **降低JavaEE API的使⽤難度**

  Spring對JavaEE API（如JDBC、 JavaMail、遠程調用等）進行了薄薄的封裝層，使這些API的使用難度大為降低。



### 2. Spring Boot



#### 框架設計初衷 

讓我們能盡可能快地跑起來Spring應用程序，並且盡可能減少配置文件的使用。



#### 核心概念 - 約定優於配置

又稱為按約定編程，是一種軟件設計泛式，指系統應該假定合理的默認值，而非要求提供不必要的配置。



#### 實現方式

1. **起步依賴**

   解決jar包衝突所導致的jar包版本管理的問題。

   使用Maven項目對象模型(Project Object Model，POM)，將能支持某個具體功能的座標打包在一起，最終提供出一個具體功能。

   例如: starter-web 就包含了 SpringMVC、Tomcat、Jackson、Validator...等 的座標。



2. **自動配置**

   解決使用Spring框架配置文件繁瑣的問題，Spring Boot 會將一些配置類註冊進IoC容器，當我們需要使用的時候，可以直接透過 @Autoweird、@Resource 註解來調用。

   註: 在只使用Spring的情況下，需要在XML中使用bean標籤進行配置，才能生成實例對象，並存到IoC容器中。

   

### 3. MyBatis

MyBatis是一款優秀的基於 **ORM(註1)** 的 **半自動(註2)** **輕量級(註3)** 持久層框架，支持客製化SQL、存儲過程與高級映射。

解決使用JDBC時，SQL語句硬編碼、手動設置參數、獲取結果集繁瑣的問題。



**名詞解釋**

1. ORM(Object/Relation Mapping) :  對象 - 關係映射，將關聯式數據庫映射至面向對象的數據抽象化技術。

   將資料庫的內容映射為對象，讓程式開發人員可以用操作對象的方式對數據庫進行操作，而不是直接使用SQL語句操作數據庫。

2. 半自動 : 需要手動編寫SQL語句。

   **註 : 全自動框架雖然方便，但我們失去了對SQL語句進行優化的權利**

3. 輕量級 : 不需要依賴第三方的軟件，運行時所需資源少。



### 4. Spring Security

一個強大的和高度可定制的身份驗證和訪問控制框架，與另一個知名安全框架Shiro相比，Spring Security的學習難度更高，但同時擁有更高的自由度。



### 5. Druid

Druid是由阿里巴巴開發的數據庫連接池，與常見的DHCP與C3P0連接池相比，性能上更加強大，並且有提供大量的監控數據，未來若要對SQL語句的調優，將會變得更加方便。







## 五、後端包結構

```text
com.ruoyi
├── common           // 工具類
│       └── constant       // 通用常量
│       └── core           // 核心控制
│       └── enums          // 通用枚舉
│       └── exception      // 通用異常
│       └── utils          // 通用類處理
│       └── xss            // XSS過濾處理
├── framework        // 框架核心
│       └── aspectj        // 註解實現
│       └── config         // 系統配置
│       └── datasource     // 數據權限
│       └── manager        // 異步處理
│       └── redis          // 緩存處理
│       └── security       // 權限控制
│       └── web            // 前端控制
├── project          // 系統模塊
│       └── common         // 通用處理
│       └── monitor        // 系統監控
│       └── system         // 系統管理
│       └── tool           // 系統工具
├── RuoYiApplication // 啟動程序
```



## 六、主要功能介紹



### 1. 使用者管理

只要是需要使用帳號、密碼登入到管理後台的人，都將被視為使用者。

![image-20200408143813382](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/08/143813-681135.png)

使用者編號 : 即為ID，不可重複。

使用者名稱 : 即為帳號，不可重複。

使用者暱稱 : 使用者的別名，可以重複。

狀態 : 如果該屬性被關閉，則該使用者將被禁止登入。



![image-20200408144703276](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/08/144815-289748.png)

**每個使用者**可以被分配 **一個部門、多個角色、多個崗位**



#### 部門 

用於控制使用者是否能夠訪問某些敏感數據，通常我們將此稱作**數據權限**。比如財務數據應該是非常敏感的，像這種資料我們就應該限制只有當使用者身處財務部門，或是財務部門的上級，才允許訪問這些敏感資料。



#### 角色

用於管理權限，權限分為兩種 

 1. 功能權限 : 與選單有關，是否可以對某個選單進行增、刪、改、查。

    例如 : 設定 普通員工角色，只能查看日誌，但不能對日誌進行增、刪、改。

 2. 數據權限 : 與部門有關，限制某些角色，不能查看敏感數據，或是只能查詢與自己部門有關的數據。

    例如 : 設定 普通員工角色，只有身處於財務部門時，才能查看財務數據；但如果是 副總經理角色，無論身處哪個部門，都可以查看財務數據。



#### 崗位 

​	只是一個身分標示符，沒有特別作用。

​	例如 : 一個使用者可以同時身兼 組長 及 工程師 的崗位。



#### 查詢當前登入的使用者

調用 SecurityUtils.getLoginUser() 方法

![image-20200410145254817](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/145255-977116.png)





### 2. 角色管理

角色用於管理權限，權限分為兩種 

 1. 功能權限 : 與選單有關，是否可以對某個選單進行增、刪、改、查。

    例如 : 設定 普通員工角色，只能查看日誌，但不能對日誌進行增、刪、改。

 2. 數據權限 : 與部門有關，限制某些角色，不能查看敏感數據，或是只能查詢與自己部門有關的數據。

    例如 : 設定 普通員工角色，只有身處於財務部門時，才能查看財務數據；但如果是 副總經理角色，無論身處哪個部門，都可以查看財務數據。

![image-20200408170104417](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/08/170104-106011.png)



#### 功能權限

![image-20200408172931310](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/08/172932-650628.png)

**角色名稱 : ** 用文字敘述該角色，方便理解。

**許可權字元 :** 

搭配註解 @PreAuthorize("@ss.hasRole('**許可權字元**')") 、@PreAuthorize("@ss.hasAnyRoles('**許可權字元**')") 做使用，可以限制某個方法，當只有使用者擁有某個角色時才能調用。

註: @ss.hasRole、@ss.hasAnyRoles 這兩種方式使用較少，通常是透過**選單許可權**來控制權限。

![image-20200408171740639](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/08/171742-684635.png)

**角色順序 : ** 顯示在角色列表時的排列順序。



##### 選單許可權 

搭配註解 @PreAuthorize("@ss.hasPermi('選單的許可權標示符')")、 @PreAuthorize("@ss.hasAnyPermi('選單的許可權標示符')")做使用，可以限制某個方法，當只有使用者所擁有的角色，具有該選單許可權時才可以調用。

![image-20200408173341195](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/08/173341-234932.png)

[查看 選單管理](#3. 選單管理)



#### 資料許可權

又稱**數據權限**，與部門有關，限制該角色，能不能查看某些部門的敏感數據。

**限制方式** 

1. 全部許可 2. 自訂許可 3. 本部門許可 4. 本部門及以下許可 5. 僅許可本人資料

例如 : 設定 普通員工角色，只有身處於財務部門時，才能查看財務部門的資料；但如果是 副總經理角色，無論身處哪個部門，都可以查看財務部門的資料。



[查看 部門管理](#4. 部門管理)

![image-20200408174518865](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/08/174525-210271.png)



**使用方式**

1. 使用@DataScope(deptAlias = "部門表別名", userAlias = "使用者表別名")註解。

2. 在 Mapper.xml 中，使用 ${dataScope} 佔位符標註在SQL語句尾部，用於附上動態SQL。

   

* 使用 deptAlias 代表，當使用者的角色，要根據使用者的部門進行限制時，將進行限制。此屬性只有對角色進行 (1) 自訂資料許可權 (2) 本部門資料許可權 (3) 部門及以下資料許可權 限制時，才有作用。

* 使用 userAlias 代表，當有限制僅能查看本人資料時，將進行限制。此屬性只有對角色進行 (1) 僅本人資料許可權 限制時，才有作用。

  

註 : 註解中的表別名必須與SQL語句中的表別名一致。

![image-20200408175434408](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/08/175437-393028.png)

部門表別名，原則上使用 d

使用者表別名，原則上使用 u

deptAlias 、 userAlias 屬性，可以擇一填寫，不用兩個都填寫。

**注意 : 原生SQL語句中，必須要有查詢 使用者表 或 部門表 時才能使用，否則會出錯。**

![image-20200413104541631](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/13/104542-430742.png)



##### 原理

1. **全部資料許可 :** 不拼接SQL語句。

2. **自訂資料許可權 :** where條件拼接此SQL語句

   ​	AND (
   ​		d.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = '使用者的角色ID' ) 
   ​	)

   根據使用者的角色，找到允許訪問的部門，來過濾結果。

3. **本部門資料許可權 :**  where條件拼接此SQL語句

   ​	AND (d.dept_id =  '使用者的部門ID')

   根據使用者所在的部門，過濾結果。

4. **本部門及以下資料許可權 :** where條件拼接此SQL語句

   ​	AND (
   ​		d.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = '使用者部門ID' OR find_in_set( '使用者部門ID', ancestors ) )
   ​	) 

   根據使用者所在部門，查找部門及子部門，過濾結果。

5. **僅本人資料許可權 :** where條件拼接此SQL語句

   ​	AND (u.user_id = '使用者ID' )

   根據使用者的ID，過濾結果。

   

### 3. 選單管理

選單管理就是 **功能管理** ，選單就是把服務所提供的功能按照列表的方式顯示出來而已。

而權限的基礎元素就是功能(將權限細化到最細，對應的就是功能)。

![image-20200409135750138](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/09/140023-963957.png)



許可權標籤 : 會與 [角色的選單許可權](#選單許可權) 對應，配置當使用者擁有某個角色時，是否可以使用該功能，在方法上使用 @PreAuthorize註解進行限制。

註 : 由於進行權限限制時，是採用硬編碼方式來撰寫，故許可權標籤請不要隨意更動。

![image-20200409140841220](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/13/090731-404482.png)



**元件路徑 :** 前端的URL地址。 

**可見 :**  在後臺畫面左側的選單列表是否允許顯示。



### 4. 部門管理

部門為樹形結構，**每個使用者需要對應一個部門**，部門可以用於限制使用者是否可以訪問敏感數據，詳細說明可以參考 [資料許可權](#資料許可權)

![image-20200409141812485](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/09/141812-880248.png)





### 5. 崗位管理

一個使用者可以有 0 ~ N 個崗位，崗位沒有特別的作用，僅僅作為一個標示符，方便管理而已。

![image-20200409142233660](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/09/142234-666287.png)

**崗位編號 :** 即為 ID，不可重複。

**崗位編碼 :** 崗位標示符，不可重複，目前還沒有什麼用。

**崗位名稱 :** 當使用者擁有崗位時，崗位將會使用崗位名稱進行顯示。



### 6. 字典管理

在資料庫中，我們有時會使用數字來代表某種狀態，比如當使用者的性別為"男"時，在資料庫中僅僅使用 0 來表示。

這樣做的好處是，當我們需要對狀態的**"顯示訊息"**進行變更的時候(EX: "男" → "男性")，不需要對所有有對性別進行判斷的地方，都做一次更新。但這樣的壞處是，將查出來的資料帶回給前端的時候，前端並不知道 0 到底代表什麼意思，這時候字典的功能就派上用場了。



當要查詢的資料，存在需要使用字典進行管理的狀態時，會發起兩次請求。

* 第一次請求，從資料庫中直接查詢出資料，不會進行狀態顯示訊息的轉換(使用者的性別為男性，傳到前端時的資料依樣是 0)。
* 第二次請求，查詢 **使用者性別字典** ，將 0 = 男，1 = 女，2=未知 的資訊帶回給前端，前端再根據使用者的性別變更**"顯示訊息"** (使用者的性別依然是用0、1、2來儲存，回傳給後端時也是一樣)。



此處截圖為 **字典資料表(SysDictData)**

![image-20200409143007496](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/09/143007-564178.png)



此處截圖為 **字典型別表(SysDictType)**

![image-20200409143819723](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/09/143819-759876.png)



要實現字典功能有兩個關鍵的表，字典資料表、字典型別表，**字典資料表** 是對應 **功能** 的，**字典型別表**是對應功能的**狀態**的，可以把 字典資料表(SysDictData) 看作 字典型別表(SysDictType) 的 **父類**。



### 7. 引數設定

即為**全局參數**設定，用資料庫的方式統一管理全局參數。

![image-20200409171243583](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/09/171244-33777.png)

引數名稱 : 用中文敘述此全局參數的用意。

引數鍵名 : 在後端要使用全局參數時，要提供的key值，不可重複。

引數鍵值 : 為全局參數的內容。

系統內建 : 標示是否為系統內建，除用於標示外無特別作用。



在程式中要使用時，透過ConfigService的selectConfigByKey("引數鍵名")，進行調用。

![image-20200409172606157](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/09/172608-22199.png)



### 8. 通知公告

一個統一的通知介面，在此可撰寫需要通知所有使用者的訊息。

![image-20200409172748314](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/09/172749-352051.png)



### 9. 操作日誌

當關鍵功能被執行時，可以使用資料庫紀錄此操作。

![image-20200409173044548](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/091534-875660.png)



**使用方式**

使用 **@Log**(title = "系統模組", businessType = "操作型別", isSaveRequestData=是否儲存請求參數) 註解，在要紀錄的方法上進行標註。

![image-20200409173227195](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/09/173443-95141.png)



**系統模組 :** 紀錄是由哪一個功能模塊操作的。

**操作型別 :** 紀錄對功能模塊的動作是什麼，由 BusinessType 枚舉類型來定義，目前內置的操作型別有

* 新增 INSERT
* 修改 UPDATE
* 刪除 DELETE
* 授權 GRANT
* 匯出 EXPORT
* 匯入 IMPORT
* 強退 FORCE
* 生成程式碼 GENCODE
* 清空資料 CLEAN
* 其他 OTHER



### 10. 登入日誌

該功能會在使用者登入時，記錄 **帳號、IP、登入地點、瀏覽器、作業系統、登入狀態、詳細資訊、時間**...等，不需要特別設定。

該功能是使用 **異步任務** 實現，可以有效降低系統負擔。

![image-20200410095851408](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/095852-419519.png)



### 11. 線上使用者

該功能可以查詢目前在系統中保持登入的使用者，使用強退功能可以讓某個使用者強制登出。

該功能是透過查詢Redis中的 **使用者令牌(Token)** 實現的。

![image-20200410100254649](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/100300-289598.png)



### 12. 定時任務

讓特定功能，每隔一段時間就自動執行。

![image-20200410100738828](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/13/090752-178713.png)



**任務名稱 :** 用文字敘述該功能的作用。

**任務組名 :** 將定時任務進行分組，方便搜尋與管理。

**狀態 :** 當定時任務為開啟狀態時，該定時任務將定時執行。

**cron 執行表示式 :** 用於定義定時任務的執行時間間隔

​	{秒數} {分鐘} {小時} {日期} {月份} {星期} {年份(可為空)}

​	0/20 * * * * ?  →  每分鐘的 0、20、40 秒都執行一次。

**呼叫目標字串 :** 定義要執行的方法，定義的方式有兩種	

1. **Spring Bean 方式呼叫**

   透過在類上使用 @Component("Spring Bean的名字") 註解，將此類交給Spring來管理，之後即可使用 **Spring Bean的名字.方法名(方法參數)** ，來定義呼叫目標字串。

   範例 : ryTask.ryMultipleParams('ry', true, 2000L, 316.50D, 100)

   **註 : 參數僅支援 字串('')，布林型別，長整型(L)，浮點型(D)，整型**

   ![image-20200410103254508](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/103257-868298.png)



2. **類全路徑名 方式呼叫**

   如果不想要定義 Spring Bean，可以採用 類全路徑名的方式，使用方法與定義Spring Bean的方式基本相同。

   範例 : com.ruoyi.quartz.task.RyTask.ryParams('ry')



點擊上方的日誌按鈕，可以查看定時任務的執行日誌。

![image-20200410134724622](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/134726-563524.png)



### 13. 資料監控

藉由阿里巴巴開發的Druid連接池，完成與程式性能相關的監控功能，主要有以下功能

1. **數據源監控 :** 監控資料庫的基本參數設定、主要的統計訊息。
2. **SQL 監控 :** 針對SQL語句的執行情況進行監控，方便排查慢SQL問題。
3. **SQL 防火牆 :** 對於黑名單、白名單防禦情況進行監控。
4. **URI 監控 :** 從API的角度紀錄API的執行時間與次數。

![image-20200410134841428](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/13/090805-216401.png)



### 14. 服務監控

顯示當前伺服器的運作情況，包含 **CPU、記憶體、JVM、硬碟** ... 等。

![image-20200410135654812](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/135655-739724.png)



### 15. 國際化

國際化訊息定義在 resource/i18n/messages.properties 中。

![image-20200410140210317](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/140212-864466.png)



![image-20200410140304888](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/140305-484780.png)

當有需要使用國際化訊息時，使用 MessageUtils.message("國際化鍵值") 來調用。

![image-20200410140358231](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/140358-813962.png)



### 16. 匯入 / 匯出 Excel

透過 @Excel 註解，管理匯入 / 匯出 的相關屬性，主要屬性有

* name : 要顯示在Excel 列表頭的欄位名稱。
* dateFormate : 日期顯示格式。
* readConvertExp : 當屬性在資料庫中是使用狀態進行記錄時，狀態與顯示訊息之間該如何轉換。
* cellType : 輸出到Excel時，欄位的類型要是數字還是字串，使用ColumnType枚舉來指定，默認為字串。
* height : 欄位的高度。
* width : 欄位的寬度。
* suffix : 文字字尾，例如 % 90 → 90% 。
* defaultValue : 當值為空時，欄位的預設值。
* prompt : 當滑鼠放到 Excel 欄位上時，出現的提示訊息。
* isExport : 是否匯出資料，應對需求:有時我們需要匯出一份模板，這是標題需要但內容需要使用者手工填寫。
* targetAttr : 當屬性為 Java Bean 的時，配合 @Excels 註解作使用，用來指定 Java Bean 中的屬性名。
* type : 用於指定是否僅限 匯入 或 匯出 時使用，默認值為不限制。

![image-20200410141536283](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/141537-94055.png)



**Excel 匯出結果**

![image-20200410144302704](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/10/144306-884736.png)





## 七、啟動專案



### 1. 對專案進行克隆

https://github.com/waiting0324/RuoYi---Traditional-Chinese

![image-20200410151848032](https://raw.githubusercontent.com/waiting0324/TyporaImg/master/typora202004/13/090820-426178.png)



![image-20200331111147386](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134822-240638.png)



### 2. 建立資料庫

2.1 在本地創建ry-vue資料庫 (默認密碼為password，如有不同需在 application-druid.yml 中變更)

![image-20200331111938267](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134825-138217.png)



2.2 進入 ruoyi/sql 資料夾，找到兩份SQL文件

![image-20200331111657174](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134839-351583.png)



2.3 在資料庫中刷入兩份SQL語句

![image-20200331112052569](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134841-376261.png)



2.4 完成資料庫建立

![image-20200331112145908](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134845-520572.png)



### 3. 啟動前端服務

3.1進入ruoyi-ui/bin資料夾，依順序執行

![image-20200331112436650](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134848-554743.png)



3.2 前端啟動成功畫面

![image-20200331112554157](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134850-119017.png)



### 4. 啟動後端服務

4.1 進入 ruoyi/bin 資料夾，依順序執行

![image-20200331112725265](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134852-440206.png)



4.2 啟動成功畫面

![image-20200331113347861](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134855-545146.png)



### 5. 啟動Redis

![image-20200331113606914](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134858-693811.png)



### 6. 訪問首頁

6.1 訪問 localhost:80

![image-20200331113821989](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134900-312595.png)

6.2 登入首頁

![image-20200331113847878](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134903-825612.png)





## 八、使用生成程式碼功能

### 0. 最終效果



**對單表的增、刪、改、查與產出Excel報表功能**

![image-20200330154211118](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134905-125393.png)



**Excel報表**

![image-20200330154637567](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134910-775275.png)



### 1. 生成程式碼

#### 1.0 示範程式碼

```sql
drop table if exists sys_student;
create table sys_student (
  student_id           int(11)         auto_increment    comment '編號',
  student_name         varchar(30)     default ''        comment '學生名稱',
  student_age          int(3)          default null      comment '年齡',
  student_sex          char(1)         default '0'       comment '性別（0男 1女 2未知）',
  student_status       char(1)         default '0'       comment '狀態（0正常 1停用）',
  student_birthday     datetime                          comment '生日',
  remark               varchar(500)    default null      comment '備註',
  primary key (student_id)
) engine=innodb auto_increment=1 comment = '學生信息表';
```



#### 1.1 將SQL語句刷入資料庫

![image-20200330140253601](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134917-436684.png)



#### 1.2 打開程式碼生成頁面

![image-20200330140455994](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134920-206825.png)



#### 1.3 找到對應的資料表

![image-20200330141013208](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134924-458799.png)



#### 1.4 打開編輯頁面

![image-20200330141125464](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134927-713730.png)



#### 1.5 編輯基本資訊

![image-20200330141332958](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134931-474656.png)



#### 1.6 編輯欄位資訊

![image-20200330145242496](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134938-179012.png)



#### 1.7 編輯生成資訊

![image-20200330145500325](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134941-136253.png)



#### 1.8 生成程式碼

![image-20200330145709111](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134944-776897.png)

#### 1.9 解壓縮產出的檔案

![image-20200330150052525](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134946-890315.png)





### 2. 佈署程式碼



#### 2.1 刷入選單SQL 

studentMenu.sql

![image-20200330150820324](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134949-595379.png)





#### 2.2 將後端程式碼放到對應目錄中

**生成的程式碼**

![image-20200330151114808](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/134954-552870.png)



**專案目錄**

<img src="https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/135327-306927.png" alt="image-20200331135326874" style="zoom:80%;" />



**生成的程式碼**

<img src="https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/135405-923315.png" alt="image-20200331135405121" style="zoom:67%;" />

**專案目錄**

<img src="https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/135421-447183.png" alt="image-20200331135420869" style="zoom:80%;" />



#### 2.3 將前端程式碼放到對應目錄中

![image-20200330151916366](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/135034-346374.png)



![image-20200330152414760](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/135049-519358.png)







#### 2.4 重啟後端

![image-20200330153522661](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/135055-722280.png)





#### 2.5 完成佈署

![image-20200330154107123](https://raw.githubusercontent.com/tony60107/TyporaImg/master/typora202003/31/135058-518266.png)



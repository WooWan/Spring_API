# Spring_API
Http 개념과 스프링에서 rest api의 설계 방법

### 들어가며.

개발을 하다 보면 항상 기본에 중요성에 대해서 많이 느끼게 되는데요. 최근에 spring을 다루다가 http에 대한 기본 지식, api 통신 방법 rest api 설계의 중요성을 느끼고 postman과 spring을 통해 이를 어떻게 다루는지 정리해보았습니다.
이번 글에 작성한 코드는 [Github](https://github.com/WooWan/Spring_API "github") 에 있습니다. 궁금한 점이나 틀린 내용이 있다면 댓글 부탁드립니다!

#### URI

<img src="https://images.velog.io/images/woohobi/post/1e5354f6-6803-40d2-a1e2-fe2969820229/image.png" width ="40%"/>

URI는 uniform resource identifier의 약자로 위 그림과 같이 url과 urn을 모두 포함합니다. 이 글에서는 url과 uri를 동일한 의미로 작성하겠습니다.


### HTTP 통신 방식 

#### 특징

http의 통신은 state less 하고 connection less 합니다.. 이 말은 http는 이전에 이루어졌던 통신에 대해서 정보가 없다는 말입니다. 이로 인해서 장점과 단점이 생기는데요

*  장점
   * 서버에 정보를 저장할 필요가 없어 서버 디자인과 서버 부하에 대해 statefull 한 방식보다 강합니다
   * 한 서버가 장애가 나도 서버가 정보를 저장하지 않기 때문에 다른 서버를 이용할 수 있습니다. 
   * connection less 하기 때문에 동시에 서버에서 처리할 리소스가 줄어듭니다.

* 단점
  * 통신할 때 이전에 통신에 대해서 독립적이기 때문에 client가 요청해야할 정보가 많습니다.
  * state가 없기 때문에 로그인 유무와 같은 정보를 저장하기 위해 session, cookie를 사용합니다.
  * SYN, SYN/ACK, ACK(3 HandsShake)를 통해 매번 연결을 맺어야 합니다.
  
  
  #### HTTP 통신
  
  <img src="https://images.velog.io/images/woohobi/post/0edd2390-f8be-405a-a97c-4d83c9fae4a0/image.png" width="50%" />
  Client와 Server는 request와 reponse 구조로 통신합니다. HTTP format은 heaer와 body로 나뉘는데 header는 body의 길이, 상태 코드, body타입 등과 같은 정보들이 들어갑니다.
  
  #### HTTP method 
  자주 사용하는 HTTP mehtod는 GET, POST, DELETE, PUT 방식이 있는데요, 이번 포스트에서는 get 방식과 post 방식에 대해서 코드로 구현해보고 자세히 알아보겠습니다.
  api를 설계할 때, rest api 관점에 따르면 사물과 목적을 분리해서 설계해야 합니다. 예를 들어 사물을 post(글) 일 때, 글을 보다, 글을 쓰다, 글을 지우다, 글을 수정하다와 같은 동작을 정의할 수 있습니다. 
 이 행위는 URL에 포함되면 안 됩니다.(물론 복잡한 현실 세계에서는 지켜지지 않을 때도 있다고하네요) 즉, create-post, delete-post 대신, /posts 에 대해서 GET, POST 와 같은 메소드가 mapping 되는 형식입니다.
  
 ##### GET
 GET 방식에서는 query parameter 형식으로 서버에 resouce를 요청하는데, 
 > google.com?name=example*quantity=20

 위와 같은 방식으로 서버에 resource를 요청하면 됩니다.
 
 ##### Post
 GET 방식과는 다르게 POST 방식에서는 일반적으로 body 부분에 데이터를 담아서 전송하게 됩니다. React, Vue와 같은 라이브러리와 통신할 때, 아래와 같은 json 형식으로 데이터가 POST 방식으로 넘어옵니다.
 ```
 {
 	"name" : "test"
 	"age" : "20"
 }
 ```
  
 
 ### Spring에서 rest api 설계
   그렇다면 spring에서 api는 어떤 방식으로 이루어지는지 알아보겠습니다

```
@Getter
@Entity
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private int itemQuantity;

    @Builder
    public Item(String title, int itemQuantity) {
        this.itemName = title;
        this.itemQuantity = itemQuantity;
    }
}
```
   
```
@Controller
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService itemsService;

    @PostMapping("/items")
    @ResponseBody
    public Long save(@RequestBody ItemsSaveDto itemsSaveDto) {
        return itemsService.save(itemsSaveDto);
    }

    @GetMapping("/items/{id}")
    @ResponseBody
    public ItemsResponseDto findById(@PathVariable Long id) {
        return itemsService.findById(id);
    }
 }
```

Controller에서 /items에 대한 GET, POST를 이 마법 같은 Spring의 annotation들을 통해 mapping 해주었습니다. 간단하게 spring에 대해서도 설명하자면,

 @GetMapping: URI에 대해서 get method에 대해서 mapping 해줍니다.
 @PostMapping: URI에 대해서 post method에 대해서 mapping 해줍니다.
 @ResponseBody: ResponseBody annotaion을 넣어주면, viewResolver가 작동하는 대신 httpMessageConverter이 String, json 자료형에 따라 해당하는 형식에 따라 response를 반환합니다. (자세한 내용은 spring에 관한 글이 아니여서 다른 글로 정리하겠습니다.)
 
 자 이제, postman을 통해서 잘 작동하는지 확인해보면,
 ##### post 요청 오레오, 수량 100개를 해주었습니다.
![](https://images.velog.io/images/woohobi/post/e5d25914-5d7b-47e3-a6b3-befd2155c83f/image.png)
##### GET 요청을 통해 요청이 정상적으로 수행되었습지 확인해보면,
![](https://images.velog.io/images/woohobi/post/a7ae2183-70e0-4c9a-8b80-09be6b2c6755/image.png)
##### 결과
![](https://images.velog.io/images/woohobi/post/fa8bf093-7d03-45c9-84f0-04661f2dc279/image.png)
id값과 함께 잘 post 요청이 이루어진 것을 확인할 수 있습니다.


### 글을 마치며.
알고리즘 글을 남기다가 개발에 관한 글은 처음인데, 글의 내용이 100이라고 한다면 작성자는 120 130은 알아야 글을 완성하는 것 같습니다.(미니멈..이라는 점ㅜㅜ) 글의 분량 상 api와 spring 모두 자세히 다루기 힘들었는데 저의 글에 나온 내용은 자그마한 부분인데 다음 글에서는 이 글에서 못다 설명한 spring 동작 방식에 대해서 글을 쓰겠습니다.!




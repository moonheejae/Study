<H2>JPA의 더티체킹</H2>
>들어가기전,
>
> JPA의 수정기능을 구현하려고 보니, 수정 관련 메서드가 없어서 정리하게된 더티체킹에 대해서 작성해보고자 한다.


<H3>더티체킹이란?</H3>
JPA의 데이터를 수정하려면 Entity를 조회하여 Entity 데이터가 변경되면 자동으로 반영이 되는 기능을 사용하는 방법이 있는데,
이를 **Dirty Checking** 이라고 한다.
<span style="color:skyblue">변경감지</span> 라고도 한다.
---

<H3>어떻게 하는가?</H3>

업데이트의 경우 save()메소드를 사용하지 않고, 수정이 가능하다.

>save()메소드는 Entity의 영속성 검증을 통해 처음들어오는 엔티티의 경우, persist()
>
>기존에 존재하는 엔티티의 경우, merge()를 한다.

<H4><span style="color:skyblue">따라서 save()에서 업데이트 기능을 제공을 하고 있는 것이다.</span></H4>

----                                                                                                  

<H3>동작방법</H3>
JPA는 영속성 Context를 생성하여 Entity의 Life Cycle을 관리한다.

JPA는 영속성 Context에 Entity를 보관할 때, 최초의 상태를 저장한다. (=스냅샷)

<span style="color:skyblue">이 Context가 Flush되는 시점에 스냅샷과 Entity 상태를 비교하여 Update해준다.


<span style="color:skyblue">**이때, 스냅샷은 Update전의 데이터로 다시 저장된다.**

---

<H3>엔티티 매니저 </H3>
1. 주입받은 엔티티 매니저를 통해 영속성 >Context에서 id를 통한 ModelEntity를 찾는다.
>Model model = entityManager.find( Model.class, ModelUpdateDto.getId() ); 
2. Model엔티티에 변경된 값을 set 해준다.
>model.setName( ModelUpdateDto.getUpdateId())
>
>*dto의 변경의도에 맞는 Update네이밍!
3. 변경감지를 하고 수정쿼리가 실행된다.

---

<H3>주의사항</H3>

- <span style="color:skyblue">@Transactional</span> 어노테이션으로 가둬줘야 변경감지가 가능하다.

  : JPA는 트랜잭션 안에서 커밋된 시점에 flush를 할 때, 엔티티의 변경된 점을 감지하고 업데이트를 해준다.

- 영속성이 무조건 있어야 한다. 비영속이나 준영속은 더티체킹이 되지 않는다.
  : 

- 변경사항만 update해주고 싶다면 @DynamicUpdate 를 엔티티에 사용해주면 된다. (성능주의)
  : 


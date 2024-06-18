<H2>PriorityQueue</H2>

<H3>**[Queue vs PriorityQueue]**</H3>

Queue는 FIFO(First In First Out) 구조로 먼저 들어간 데이터가 먼저 나오는 구조다.

PriorityQueue는 우선순위가 높은 데이터가 먼저 나오는 구조다.

---
<H3>**[선언방법]**</H3>

-  작은 수가 우선순위 높음
>PriorityQueue<Integer> pQ = new PriorityQueue<>();

- 큰수가 우선순위 높음 
> PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a,b) -> b-a);
> 
> PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

---

<H3>**[메서드]**</H3>

>offer() : 큐에 값 추가, 실패시 false
> 
>add()  : 큐에 값 추가, 꽉 찬 경우 IllegalStateException
> 
>poll() : 첫번째 값 반환하고 제거 or null
> 
>remove() :  첫번째 값 반환하고 제거 or IllegalStateException

---

<H3>**[예제]**</H3>

```java
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<Integer> pQ = new PriorityQueue<>();
        pQ.add(1);
        pQ.add(3);
        pQ.add(2);
        pQ.offer(5);
        pQ.offer(4);

        while (!pQ.isEmpty()) {
            System.out.println(pQ.poll()); // 첫번째 값 반환 후 제거
        }
    }
}
```
<H3>**[결과]**</H3>
```
1
2
3
4
5
```
---
<H3>**[참고]**</H3>
- PriorityQueue는 기본적으로 오름차순이기 때문에 내림차순으로 사용하려면 Comparator를 사용해야 한다. 
- PriorityQueue는 힙을 사용하기 때문에 시간복잡도가 O(logN)이다.
- 데이터 정렬이나 검색이 아닌 우선순위 데이터 검색과 삭제에 유용한 자료구조이다.
- Binary Heap을 사용하여 구현되어 있어서 데이터 중복값이 존재할 수 있다.
---

<H3>**[관련문제]**</H3>
백준 11279번 최대 힙 : https://www.acmicpc.net/problem/11279 
```
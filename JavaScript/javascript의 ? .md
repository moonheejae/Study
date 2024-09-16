<h2>Javascript의 false와 falsy</h2>

---


<h3>JavaScript에서 falsy란?</h3> 

`false`, `null`, `undefined`, `0`, `NaN`, `""` (빈 문자열)을 말한다. 

그 반대로는 **truthy**로 취급한다.

<br/>

<h3>JavaScript의 false란? </h3>

말그대로 boolean의 false값을 말한다. 

---

<h3>[ false ]</h3>
```
messageOutput.content == null ? "" : messageOutput.content
```

위 코드는 `messageOutput.content`가 `null`일 때 빈 문자열 `""`을 반환하고, 그렇지 않으면 `messageOutput.fileUrl`의 값을 반환한다. 

즉, null만  체크한다.

---

<h3>[ falsy ]</h3>
```
messageOutput.content? "" : messageOutput.content
```

이 코드는 `messageOutput.content`이 "truthy"한 값이면 빈 문자열 `""`을 반환하고, "falsy"한 값이면 `messageOutput.content`을 반환한다.


즉, `null`뿐만 아니라, `undefined`, 빈 문자열, `0` 등 모든 "falsy" 값을 체크한다.

---


`data.content`의 모든 **falsy 값** (`null`, `undefined`, `''` (빈 문자열), `0`, `false`, `NaN`)을 체크하려면 ?

```
if (!data.content) { }
```
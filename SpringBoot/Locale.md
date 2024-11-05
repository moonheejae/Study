
<h1>국제화 처리 : i18n</h1>   
<h3>스프링부트의 MessageSource를 이용하여 언어별 페이지를 구현한다.</h3>


<hr>
<h2>설정</h2>

### .yml 
````
spring: 
    messages:
        encoding: UTF-8
        use-code-as-default-message: true # 코드값 못 찾으면 그대로 반환 / false 경우, exception 발생
        fallback-to-system-locale: true # config 에서 설정한 언어에 맞는 파일 활용
        basename: messages  # 해당 경로시, resources 하위읽음 (디렉토리 추가시 'messagesDir/messages'이런식으로 디렉토리면 따로 명시  
````

### .properties
>resources <-해당 폴더 하위에 아래 파일들이 존재한다. 
 >> messages.properties <br>
> messages_ko.properties <br>
> messages_en.properties

내부의 값들은 key=value 형태로 저장한다. 

<br>
<hr>


## MessageConfig.java

````~~java
package com.hee.test.config.message;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class MessageConfig implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver() { 
        // Locale초기화 및 default값 셋팅 
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.KOREA);
        
        return sessionLocaleResolver;
    }
    /**
    * 인터셉터에서 앞단의 lang값 변경시, Locale값을 변경한다. 
    **/
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(localeChangeInterceptor());
    }
}
````

## MessageController.java
````java

package com.hee.test.config.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@CrossOrigin("*")
@Slf4j
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private final MessageSource messageSource;
    
    @GetMapping("/i18n/messages")
    public Map<String, String> getMessages(@RequestParam("keys") String keys){
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, String> resultMap = new HashMap<>();

        String[] keysArray = keys.split(",");
        for (String key : keysArray) {
            String message = messageSource.getMessage(key, null, locale);
            resultMap.put(key, message);
        }
        return resultMap;
    }
}
````

<br/>
<hr>

## js
````javascript
function changeLocale(){
    // 호출시, 인터셉터에서 Locale lang값 설정
    $.ajax({
        type: 'GET',
        url: deFaultUrl,
        data: {
            lang: Lang
        },
        success: function() {
            console.log('language setting completed!');
        },
        error: function(xhr, status, error) {
            console.error('Failed to load language settings.');
        }
    });
}

function loadMessages() {
    var i18nKeys = $('[data-i18n]').map(function() {
        return $(this).data('i18n');
    }).get();
    // data-value 형태로 key값을 넣어주고, 해당 페이지의 값들을 가져와 ajax
    $.ajax({
        type: 'GET',
        url: deFaultUrl + 'i18n/messages',
        data: {
            keys: i18nKeys.join(',')
        },
        success: function(response) {
            $('[data-i18n]').each(function() {
                var i18nKey = $(this).data('i18n');
                if (response[i18nKey]) {
                    $(this).text(response[i18nKey]);
                }
            });
        },
        error: function(xhr, status, error) {
            console.error('Failed to load language settings.', xhr.responseText);
        }
    });
}
````

<hr>

### 마무리 

>AcceptHeaderLocaleResolver : Http Header Accept-Language 사용
<br>FixedLocaleResolver :  default locale 사용
> <br>CookieLocaleResolver : 쿠키값 사용
<br>SessionLocaleResolver : 세션값 사용


이들의 차이에 대해 알고, 이에 맞는 걸로 사용하자.
package com.cknb.htTotalApp.translation.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GoogleTranslateService {

    @Value("${google.credentials.key}")
    private String GOOGLE_TRANSLATE_KEY;
    private static final int CHUNK_SIZE = 5000; // 구글 번역 API의 최대 텍스트 길이
    
    public String translate(String text, String targetCountry, String targetLang) {
        StringBuilder translatedText = new StringBuilder();
        List<String> chunks = splitText(text);
        // 국가 코드 -> 언어 코드
        String sourceLang = googleTranslateService.countryToLang(targetCountry);

        for (String chunk : chunks) {
            String translatedChunk = this.translateText(chunk, sourceLang, targetLang);
            translatedText.append(decodeHTMLEntities(translatedChunk));
        }
        return translatedText.toString();
    }
    
    /**
     * 번역 기능
     * */
    public String translateText(String content, String sourceLang, String contentLang){
        Translate trans =  TranslateOptions.newBuilder().setApiKey(GOOGLE_TRANSLATE_KEY).build().getService();
        //번역
        Translation translation = trans.translate(content//번역할 내용
                , Translate.TranslateOption.sourceLanguage(contentLang)//원본 언어
                , Translate.TranslateOption.targetLanguage(sourceLang));//번역할 언어

        //번역 내용 전달
        return translation.getTranslatedText();
    }

    /**
     * 특수 문자 처리
     * **/
    public String decodeHTMLEntities(String input) {
        String[][] patternsAndReplacements = {
                {"&quot;", "\""},
                {"&amp;", "&"},
                {"&lt;", "<"},
                {"&gt;", ">"},
                {"&#39;", "'" }
        };
        String result = input;
        for (String[] patternAndReplacement : patternsAndReplacements) {
            String pattern = patternAndReplacement[0];
            String replacement = patternAndReplacement[1];
            result = result.replaceAll(Pattern.quote(pattern), replacement);
        }

        return result;
    }

    /**
     * 번역 텍스트가 5000자 이상인 경우, 최대 길이 수를 초과해 번역 안되는 오류 발생
     * 해결 : 청크(chunk)로 분리하여 번역 처리
     * */
    private List<String> splitText(String text) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + CHUNK_SIZE, text.length());
            chunks.add(text.substring(start, end));
            start = end;
        }
        return chunks;
    }

    private String countryToLang(String userCountry) {
        userCountry = userCountry.toLowerCase();

        //5개국: 주요 서비스 제공
        if(userCountry.equals("kr") || userCountry.equals("kp")){ return "ko"; }//한국어
        if(userCountry.equals("jp")){ return "ja"; }//일본어
        if(userCountry.equals("cn") || userCountry.equals("tw") || userCountry.equals("mo") || userCountry.equals("hk")){ return "zh-CN"; }//중국어(간체)
        if(userCountry.equals("vn")){ return "vi"; }//베트남어
        if(userCountry.equals("th")){ return "th"; }//태국어
        //129개국: iOS 서비스 제공
        if(userCountry.equals("ae") || userCountry.equals("sa") || userCountry.equals("lb") || userCountry.equals("ly") || userCountry.equals("ma") || userCountry.equals("mr") || userCountry.equals("bh") || userCountry.equals("eh") || userCountry.equals("sd") || userCountry.equals("sy") || userCountry.equals("dz") || userCountry.equals("ye") || userCountry.equals("om") || userCountry.equals("jo") || userCountry.equals("iq") || userCountry.equals("eg") || userCountry.equals("qa") || userCountry.equals("kw") || userCountry.equals("tn") || userCountry.equals("ps")){ return "ar"; }//아랍어
        if(userCountry.equals("am")){ return "hy"; }//아르메니아어
        if(userCountry.equals("az")){ return "az"; }//아제르바이잔어
        if(userCountry.equals("bg")){ return "bg"; }//불가리아어
        if(userCountry.equals("bo")){ return "qu"; }//케추아어
        if(userCountry.equals("by")){ return "be"; }//벨라루스어
        if(userCountry.equals("cz")){ return "cs"; }//체코어
        if(userCountry.equals("de") || userCountry.equals("gl") || userCountry.equals("li") || userCountry.equals("ch") || userCountry.equals("at")){ return "de"; }//독일어
        if(userCountry.equals("dk") || userCountry.equals("fo")){ return "da"; }//덴마크어
        if(userCountry.equals("ee")){ return "et"; }//에스토니아어
        if(userCountry.equals("es") || userCountry.equals("gt") || userCountry.equals("ni") || userCountry.equals("dm") || userCountry.equals("do") || userCountry.equals("mx") || userCountry.equals("ve") || userCountry.equals("ar") || userCountry.equals("ec") || userCountry.equals("sv") || userCountry.equals("hn") || userCountry.equals("uy") || userCountry.equals("gq") || userCountry.equals("cl") || userCountry.equals("cr") || userCountry.equals("co") || userCountry.equals("cu") || userCountry.equals("pa") || userCountry.equals("pe") || userCountry.equals("pr")){ return "es"; }//스페인어
        if(userCountry.equals("fi")){ return "fi"; }//핀란드어
        if(userCountry.equals("fr") || userCountry.equals("tf") || userCountry.equals("gf") || userCountry.equals("pf") || userCountry.equals("ga") || userCountry.equals("gp") || userCountry.equals("gn") || userCountry.equals("nc") || userCountry.equals("ne") || userCountry.equals("re") || userCountry.equals("yt") || userCountry.equals("mq") || userCountry.equals("mc") || userCountry.equals("bj") || userCountry.equals("be") || userCountry.equals("bi") || userCountry.equals("bf") || userCountry.equals("bv") || userCountry.equals("mf") || userCountry.equals("bl") || userCountry.equals("sn") || userCountry.equals("pm") || userCountry.equals("wf") || userCountry.equals("cf") || userCountry.equals("dj") || userCountry.equals("td") || userCountry.equals("ci") || userCountry.equals("cg") || userCountry.equals("cd") || userCountry.equals("tg")){ return "fr"; }//프랑스어
        if(userCountry.equals("ge")){ return "ka"; }//조지아어
        if(userCountry.equals("gr") || userCountry.equals("cy")){ return "el"; }//그리스어
        //나머지: 구글 번역 제공
        if(userCountry.equals("ad")){ return "ca"; }//카탈루냐어
        if(userCountry.equals("af")){ return "ps"; }//파슈토어
        if(userCountry.equals("al") || userCountry.equals("xk")){ return "sq"; }//알바니아어
        if(userCountry.equals("ba")){ return "bs"; }//보스니아어
        if(userCountry.equals("bd")){ return "bn"; }//벵골어
        if(userCountry.equals("dv")){ return "dv"; }//디베히어
        if(userCountry.equals("er")){ return "ti"; }//티그리냐어
        if(userCountry.equals("et")){ return "am"; }//암하라어
        if(userCountry.equals("gh")){ return "ee"; }//에웨어
        if(userCountry.equals("hr")){ return "hr"; }//크로아티아어
        if(userCountry.equals("ht")){ return "ht"; }//아이티 크리올어
        if(userCountry.equals("hu")){ return "hu"; }//헝가리어
        if(userCountry.equals("id")){ return "id"; }//인도네시아어
        if(userCountry.equals("ie")){ return "ga"; }//아일랜드
        if(userCountry.equals("il")){ return "iw"; }//히브리어
        if(userCountry.equals("in")){ return "hi"; }//힌디어
        if(userCountry.equals("ir")){ return "fa"; }//페르시아어
        if(userCountry.equals("is")){ return "is"; }//아이슬란드어
        if(userCountry.equals("it") || userCountry.equals("va") || userCountry.equals("sm")){ return "it"; }//이탈리아어
        if(userCountry.equals("ke") || userCountry.equals("km") || userCountry.equals("tz")){ return "sw"; }//스와힐리어
        if(userCountry.equals("kg")){ return "ky"; }//키르기스어
        if(userCountry.equals("kh")){ return "km"; }//크메르어
        if(userCountry.equals("kz")){ return "kk"; }//카자흐어
        if(userCountry.equals("lo")){ return "lo"; }//라오어
        if(userCountry.equals("ls")){ return "st"; }//세소토어
        if(userCountry.equals("lt")){ return "lt"; }//리투아니아어
        if(userCountry.equals("lu")){ return "lb"; }//룩셈부르크어
        if(userCountry.equals("mg")){ return "mg"; }//말라가시어
        if(userCountry.equals("mk")){ return "mk"; }//마케도니아어
        if(userCountry.equals("ml")){ return "bm"; }//밤바라어
        if(userCountry.equals("mm")){ return "my"; }//미얀마어(버마어)
        if(userCountry.equals("mn")){ return "mn"; }//몽골어
        if(userCountry.equals("mt")){ return "mt"; }//몰타어
        if(userCountry.equals("my") || userCountry.equals("cc")){ return "ms"; }//말레이어
        if(userCountry.equals("nl") || userCountry.equals("bq") || userCountry.equals("sr") || userCountry.equals("aw")){ return "nl"; }//네덜란드어
        if(userCountry.equals("no") || userCountry.equals("sj")){ return "no"; }//노르웨이어
        if(userCountry.equals("np")){ return "ne"; }//네팔어
        if(userCountry.equals("nz")){ return "mi"; }//마오리어
        if(userCountry.equals("ph")){ return "tl"; }//타갈로그어(필리핀어)
        if(userCountry.equals("pk")){ return "ur"; }//우르두어
        if(userCountry.equals("pl")){ return "pl"; }//폴란드어
        if(userCountry.equals("pt") || userCountry.equals("br") || userCountry.equals("cv") || userCountry.equals("tl") || userCountry.equals("mz") || userCountry.equals("st") || userCountry.equals("ao")){ return "pt"; }//포르투갈어(포르투갈, 브라질)
        if(userCountry.equals("py")){ return "gn"; }//과라니어
        if(userCountry.equals("ro") || userCountry.equals("md")){ return "ro"; }//루마니아어
        if(userCountry.equals("rs") || userCountry.equals("me")){ return "sr"; }//세르비아어
        if(userCountry.equals("ru") || userCountry.equals("lv")){ return "ru"; }//러시아어
        if(userCountry.equals("rw")){ return "rw"; }//키냐르완다어
        if(userCountry.equals("se") || userCountry.equals("ax")){ return "sv"; }//스웨덴어
        if(userCountry.equals("si")){ return "sl"; }//슬로베니아어
        if(userCountry.equals("sk")){ return "sk"; }//슬로바키아어
        if(userCountry.equals("so")){ return "so"; }//소말리어
        if(userCountry.equals("tj")){ return "tg"; }//타지크어
        if(userCountry.equals("tm")){ return "tk"; }//투르크멘어
        if(userCountry.equals("tr")){ return "tr"; }//튀르키예어
        if(userCountry.equals("ua")){ return "uk"; }//우크라이나어
        if(userCountry.equals("uz")){ return "uz"; }//우즈베크
        if(userCountry.equals("ws") || userCountry.equals("as")){ return "sm"; }//사모아어
        if(userCountry.equals("za")){ return "af"; }//아프리칸스어
        //그 외
        return "en";
    }
}

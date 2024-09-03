@RestController
@Slf4j
@Tag(name = "TranslateController", description = "google translaton API")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/translation")
public class TranslateController {

    private final GoogleTranslateService googleTranslateService;

    @PostMapping
    @Operation(summary = "google translaton API", description = "google translaton API")
    public ResponseEntity<BaseRepsonseDto<HashMap<String, Object>>> translateContext(
            @Valid @RequestBody RequestTranslationDto dto
    ) {
        String toLang = googleTranslateService.countryToLang(dto.getUserCountry());
        HashMap<String, Object> param = new HashMap<>();
        HashMap<String, Object> result = new HashMap<>();

        if (dto.getContent() != null && !dto.getContent().isEmpty()) {
            param.put("content", dto.getContent());
        }

        // 번역 항목 추가
        for (String key : param.keySet()) {
            result.put(key, googleTranslateService.translate(param.get(key).toString(), dto.getUserCountry(), dto.getContentLang()));
        }
        return ResponseEntity.ok(
                BaseRepsonseDto.<HashMap<String, Object>>builder()
                        .statusCode(200)
                        .status("정상")
                        .data(result)
                        .build()
        );
        
    }

    
}
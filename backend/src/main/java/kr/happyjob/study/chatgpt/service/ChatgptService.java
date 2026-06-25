package kr.happyjob.study.chatgpt.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.client.RestClientResponseException;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import kr.happyjob.study.common.comnUtils.FileUtilCho;

import org.springframework.http.HttpHeaders;

@Service
public class ChatgptService {

	// Set logger
	private final Logger logger = LogManager.getLogger(this.getClass());

	@Value("${fileUpload.rootPath}")
	private String rootPath;
	
	@Value("${fileUpload.temp}")
	private String tempPath;
	
	@Value("${fileUpload.virtualRootPath}")
	private String virtualRootPath;
	
	// Get class name for logger
	private final String className = this.getClass().toString();

	private  RestTemplate restTemplate;
    private final String chatGptApiUrl = "https://api.openai.com/v1/chat/completions";

    public ChatgptService() {
        this.restTemplate = new RestTemplate();
    }

    public String getChatResponse(String userInput, String apiKey) {
        // 1) 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);

        // 2) 바디(JSON) — system + user로 정상 구성
        ObjectMapper om = new ObjectMapper();
        ObjectNode body = om.createObjectNode();
        body.put("model", "gpt-4o"); // 실제 사용 모델명으로 교체
        ArrayNode msgs = body.putArray("messages");
        msgs.add(om.createObjectNode()
                .put("role", "system")
                .put("content", "You are a helpful assistant."));
        msgs.add(om.createObjectNode()
                .put("role", "user")
                .put("content", userInput));

        String requestBody;
        try {
            requestBody = om.writeValueAsString(body);
        } catch (Exception e) {
            return "{\"result\":\"N\",\"error\":\"request serialize failed: " + e.getMessage() + "\"}";
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> resp =
                    restTemplate.exchange(chatGptApiUrl, HttpMethod.POST, entity, String.class);

            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                return "{\"result\":\"N\",\"error\":\"non-2xx: " + resp.getStatusCodeValue() + "\"}";
            }

            // 3) 응답에서 content 추출
            String bodyStr = resp.getBody();
            com.fasterxml.jackson.databind.JsonNode root = om.readTree(bodyStr);
            com.fasterxml.jackson.databind.JsonNode contentNode =
                    root.path("choices").path(0).path("message").path("content");

            String content = contentNode.isTextual() ? contentNode.asText() : bodyStr;

            // 단순 텍스트만 반환(프런트가 바로 출력하기 쉬움)
            return content;

            // 필요하면 래퍼 JSON으로:
            // return om.writeValueAsString(java.util.Collections.singletonMap("response", content));

        } catch (RestClientResponseException ex) {
            // OpenAI 쪽 에러 바디 표시
            String errBody = ex.getResponseBodyAsString();
            return "{\"result\":\"N\",\"error\":\"" + ex.getRawStatusCode() + "\",\"body\":" +
                    om.valueToTree(errBody).toString() + "}";

        } catch (Exception e) {
            return "{\"result\":\"N\",\"error\":\"" + e.getClass().getSimpleName() + ": " + e.getMessage() + "\"}";
        }
    }
    
    public String getChatResponse4(String userInput,String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);  

        String requestBody = "{\"model\":\"gpt-5\",\"messages\":[{\"role\":\"system\",\"content\":\"" + userInput + "\"}]}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // ChatGPT API 호출
        ResponseEntity<String> responseEntity=null;
        try{
           responseEntity = restTemplate.exchange(chatGptApiUrl, HttpMethod.POST, requestEntity, String.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        // ChatGPT API 응답 처리
        String chatResponse = responseEntity.getBody();

        return chatResponse;
    }    
    
    
    /* ---------------------------------------------------------------
     *  OpenAI Chat+Vision 통합 호출
     * ------------------------------------------------------------- */
    public String getChatResponse4file(String userInput,
                                       HttpServletRequest request,
                                       String apiKey,          // ⬅️ 텍스트 Chat Key
                                       String visionKey)       // ⬅️ Vision  Key
                                       throws Exception {

    	/* ① 업로드 파일 체크 */
        MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest) request;
        Map<String, Object> fileInfo = new FileUtilCho(
                multiReq, rootPath, virtualRootPath, tempPath + File.separator)
                .uploadFiles();                                          

        /* ② 텍스트-Chat 헤더 */
        HttpHeaders chatHeaders = new HttpHeaders();
        chatHeaders.setContentType(MediaType.APPLICATION_JSON);
        // Bearer 수동 설정
        chatHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);

        /* ③ Vision 헤더 */
        HttpHeaders visionHeaders = new HttpHeaders();
        visionHeaders.setContentType(MediaType.APPLICATION_JSON);
        // ⬇️ setBearerAuth 대신 수동 설정
        visionHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + visionKey);

        /* ④ 파일 여부 분기 */
        long size = 0L;
        if (fileInfo.get("file_size") != null) {
            size = Long.parseLong(String.valueOf(fileInfo.get("file_size")));
        }

        if (size > 0) {                                  // ✔ 이미지가 있을 때
            String filePath = (String) fileInfo.get("file_loc");
            return callVision(userInput, new File(filePath), visionHeaders);
        }

        /* ⑤ 이미지가 없으면 일반 Chat */
        return callChat(userInput, chatHeaders);
    }

    /* ---------------------------------------------------------------
     *  일반 Chat 호출 (텍스트만)
     * ------------------------------------------------------------- */
    private String callChat(String userInput,
                            HttpHeaders headers) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        
        Map<String, Object> role1 = new HashMap<String, Object>();
        Map<String, Object> role2 = new HashMap<String, Object>();
        
        role1.put("role", "system");
        role1.put("content", "You are a helpful assistant.");
        
        role2.put("role", "user");
        role2.put("content", userInput);
        
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(role1);
        messages.add(role2);
        
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("model", "gpt-5");
        body.put("temperature", 0.7);
        body.put("messages", messages);
        
        /*
        Map<String, Object> body = Map.of(
            "model", "gpt-4o",
            "temperature", 0.7,
            "messages", List.of(
                Map.of("role", "system",
                       "content", "You are a helpful assistant."),
                Map.of("role", "user",
                       "content", userInput)
            )
        );
        */
        HttpEntity<String> req =
            new HttpEntity<>(om.writeValueAsString(body), headers);

        try {
            ResponseEntity<String> res = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions", req, String.class);
            return res.getBody();
        } catch (HttpClientErrorException e) {
            return "[Chat API 오류] " + e.getStatusCode()
                   + " : " + e.getResponseBodyAsString();
        }
    }

    /* ---------------------------------------------------------------
     *  Vision 호출 (이미지 + 텍스트)
     * ------------------------------------------------------------- */
    private String callVision(String userInput,
                              File imageFile,
                              HttpHeaders headers) throws IOException {

        ObjectMapper om = new ObjectMapper();

        /* 1) 이미지 Base64 인코딩 */
        String base64 = Base64.getEncoder()
                .encodeToString(Files.readAllBytes(imageFile.toPath()));

        /* 2) 요청 Body 구성 */

        // image_url 노드
        Map<String, Object> innerImageUrl = new HashMap<>();
        innerImageUrl.put("url", "data:image/jpeg;base64," + base64);

        Map<String, Object> imageNode = new HashMap<>();
        imageNode.put("type", "image_url");
        imageNode.put("image_url", innerImageUrl);

        // text 노드
        Map<String, Object> textNode = new HashMap<>();
        textNode.put("type", "text");
        textNode.put("text", userInput);

        // content 배열
        List<Object> contentList = new ArrayList<>();
        contentList.add(textNode);
        contentList.add(imageNode);

        // message 객체
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", contentList);

        // messages 리스트
        List<Object> messages = new ArrayList<>();
        messages.add(message);

        // 최종 body
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-5");
        body.put("messages", messages);

        HttpEntity<String> req =
                new HttpEntity<>(om.writeValueAsString(body), headers);

        /* 3) Vision API 호출 */
        try {
            ResponseEntity<String> res = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    req,
                    String.class
            );
            return res.getBody();

        } catch (HttpClientErrorException e) {
            return "[Vision API 오류] "
                    + e.getStatusCode() + " : "
                    + e.getResponseBodyAsString();
        }
    }

    
    
}
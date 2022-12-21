//package TaskManager.controller;
//
//import org.springframework.http.*;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//@RequestMapping("/gitAuth")
//public class GitAuthController {
//    String client_id="f21614ae68732a9a2cc0";
//    String client_secret="a62b7f0991686edd80a37b4b2aa63411df873fb0";
//    String baseLink = "https://github.com/login/oauth/access_token?";
//
//    /* TODO: implement on front - The Get request URL and return
//        code: https://github.com/login/oauth/authorize?client_id=f21614ae68732a9a2cc0&scope=user:email */
//
//    @RequestMapping(method = RequestMethod.POST, path = "/getToken")
//    public String getTokenFromGitFrontend(@RequestParam String code) {
//        System.out.println("##########");
//
//        String linkGetToken = baseLink + "client_id=" + client_id + "&client_secret=" + client_secret + "&code=" + code;
//        ResponseEntity<TokenResponse> result = reqGitGetToken(linkGetToken);
//
//        String token = result.getBody().getAccess_token();
//        //do some check's
//        System.out.println(token);
//
//        return linkGetToken;
//    }
//
//
//    public static ResponseEntity<TokenResponse> reqGitGetToken(String link) {  //send link return response
//        ResponseEntity<TokenResponse> response = null;
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Accept", "application/json");
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//        try {
//            return restTemplate.exchange(link, HttpMethod.POST, entity, TokenResponse.class);
//        } catch (Exception e) {
//            System.out.println("");
//        }
//        return null;
//    }
//}

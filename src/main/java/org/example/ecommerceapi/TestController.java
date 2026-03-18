package org.example.ecommerceapi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger log = LogManager.getLogger(TestController.class);

    @GetMapping("/test")
    public String test() {
        log.info("Test API called");
        log.error("Example error log");
        return "OK";
    }
}
package kr.ron2.restdocspractice.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SampleController {

    @PostMapping("/users")
    public SampleResponse getSampleResponse(@RequestBody SampleRequest sampleRequest) {
        log.info(sampleRequest.getName());
        return SampleResponse.from(sampleRequest);
    }
}

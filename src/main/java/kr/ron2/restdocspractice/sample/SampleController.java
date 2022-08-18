package kr.ron2.restdocspractice.sample;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @PostMapping("/users")
    public SampleResponse getSampleResponse(@RequestBody SampleRequest sampleRequest) {

        return SampleResponse.from(sampleRequest);
    }
}

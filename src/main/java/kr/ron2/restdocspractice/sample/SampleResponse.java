package kr.ron2.restdocspractice.sample;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SampleResponse {

    private final Long id;
    private final String name;

    public static SampleResponse from(SampleRequest sampleRequest) {
        return new SampleResponse(sampleRequest.getId(), sampleRequest.getName());
    }
}

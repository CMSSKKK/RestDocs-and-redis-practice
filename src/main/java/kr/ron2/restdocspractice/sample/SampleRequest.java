package kr.ron2.restdocspractice.sample;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class SampleRequest {

    private final Long id;
    private final String name;


}

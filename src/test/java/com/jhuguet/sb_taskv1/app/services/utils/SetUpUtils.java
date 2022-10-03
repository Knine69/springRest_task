package com.jhuguet.sb_taskv1.app.services.utils;

import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Tag;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
public class SetUpUtils {
    public GiftCertificate sampleCertificate() {
        return new GiftCertificate("AWS Certificate",
                "AWS Master's certificate",
                BigDecimal.valueOf(8.99),
                10,
                "2022-09-20T14:33:15.1301054",
                "2022-09-20T14:33:15.1301054",
                Stream.of(new Tag("Cloud"))
                        .collect(Collectors.toCollection(HashSet::new))
        );
    }

    public Tag sampleTag() {
        return new Tag("Cloud", new HashSet<>(sampleCertificates()));
    }

    public List<Tag> sampleTags() {
        return List.of(
                sampleTag(),
                sampleTag()
        );
    }

    public List<GiftCertificate> sampleCertificates() {
        return List.of(
                sampleCertificate(),
                sampleCertificate()
        );
    }
}

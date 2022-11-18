package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.IdAlreadyInUse;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.exceptions.NotAuthorized;
import com.jhuguet.sb_taskv1.app.exceptions.PageNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.UnqualifiedAuthority;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface GiftCertificateService {
    Page<GiftCertificate> getAllPageable(Pageable pageable) throws PageNotFound;

    GiftCertificate get(int id) throws IdNotFound;

    Page<GiftCertificate> filterCertificates(String tagName,
                                             String nameOrDescriptionPart,
                                             String nameOrDate,
                                             String order,
                                             Pageable pageable) throws PageNotFound;

    GiftCertificate save(GiftCertificate certificate) throws MissingEntity, InvalidInputInformation, IdAlreadyInUse;

    GiftCertificate update(int id, Map<String, Object> patch) throws IdNotFound, InvalidInputInformation;

    GiftCertificate delete(int id) throws IdNotFound;

    Order placeNewOrder(List<Integer> certId, int userId) throws IdNotFound;

    void checkIdentity(String jwt, int id, boolean requiresAdmin) throws NotAuthorized, IOException,
            UnqualifiedAuthority;

}

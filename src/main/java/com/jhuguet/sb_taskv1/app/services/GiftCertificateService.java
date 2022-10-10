package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Tag;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GiftCertificateService {
    List<GiftCertificate> getAll();

    GiftCertificate get(int id) throws IdNotFound, InvalidIdInputInformation;

    List<GiftCertificate> getByTagName(String name);

    List<GiftCertificate> getByPart(String part);

    List<GiftCertificate> getByDateOrName(String sortBy, String order);

    GiftCertificate save(GiftCertificate certificate);

    GiftCertificate update(int id, Map<String, Object> patch) throws IdNotFound, InvalidIdInputInformation, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    GiftCertificate delete(int id) throws IdNotFound, InvalidIdInputInformation;

}

package com.jhuguet.sb_taskv1.app.services;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService {
    List<GiftCertificate> getAll();

    GiftCertificate get(int id) throws IdNotFound;

    List<GiftCertificate> filterCertificates(String tagName, String nameOrDescriptionPart, String nameOrDate, String order);

    GiftCertificate save(GiftCertificate certificate) throws MissingEntity;

    GiftCertificate update(int id, Map<String, Object> patch) throws IdNotFound, InvalidInputInformation;

    GiftCertificate delete(int id) throws IdNotFound;

    GiftCertificate placeNewOrder(int certID, int userID) throws IdNotFound;
    GiftCertificate placeNewInOrder(int certID, int orderID, int userID) throws IdNotFound;

}

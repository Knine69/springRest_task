package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.TagsAssociatedException;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping(path = "/certificate")
public class GiftCertificateController {

    private Logger logger = Logger.getLogger(GiftCertificateController.class.getName());

    @Autowired
    private GiftCertificateService giftCertificateService;

    @ResponseBody
    @GetMapping("/")
    public List<GiftCertificate> getAllCertificates() {
        return giftCertificateService.getAllCertificates();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public GiftCertificate getCertificateById(@PathVariable String id) throws IdNotFound, InvalidIdInputInformation {
        return giftCertificateService.getCertificate(Integer.valueOf(id));
    }

    @PostMapping("/newCertificate")
    public void saveGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.saveCertificate(giftCertificate);
        logger.info("Successfully saved new certificate into db");
    }

    @PostMapping("/tagToCert/{certId}/tag/{tagId}")
    public void saveTagToCert(@PathVariable("certId") String certId, @PathVariable("tagId") String tagId) throws IdNotFound, InvalidIdInputInformation {
        giftCertificateService.addTagToCertificate(Integer.valueOf(certId), Integer.valueOf(tagId));
        logger.info("Successfully saved new tag to certificate");
    }

    @PutMapping("/update")
    public GiftCertificate updateGiftCertificate(@RequestBody GiftCertificate giftCertificate) throws IdNotFound, InvalidIdInputInformation {
        logger.info("Updating certificate: " + giftCertificate.getId());
        return giftCertificateService.updateCertificate(giftCertificate);
    }

    @DeleteMapping("/drop/{id}")
    public GiftCertificate deleteCertificate(@PathVariable String id) throws IdNotFound, InvalidIdInputInformation, TagsAssociatedException {
        logger.info("Deleting certificate: " + id);
        return giftCertificateService.deleteCertificate(Integer.valueOf(id));
    }
}

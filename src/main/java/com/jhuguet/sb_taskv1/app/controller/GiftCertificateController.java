package com.jhuguet.sb_taskv1.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.jhuguet.sb_taskv1.app.exceptions.BaseException;
import com.jhuguet.sb_taskv1.app.exceptions.CertificateAssociatedException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.services.GiftCertificateService;
import com.jhuguet.sb_taskv1.app.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/certificate")
public class GiftCertificateController {

    private Logger logger = Logger.getLogger(GiftCertificateController.class.getName());

    private GiftCertificateService giftCertificateService;
    private TagService tagService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, TagService tagService) {
        this.giftCertificateService = giftCertificateService;
        this.tagService = tagService;
    }

    @ResponseBody
    @GetMapping({"/", "/{id}"})
    public List<GiftCertificate> getCertificateById(@PathVariable(required = false) String id) throws IdNotFound, InvalidIdInputInformation {
        if (id != null) {
            List<GiftCertificate> returnCertificate = new ArrayList<>();
            returnCertificate.add(giftCertificateService.get(Integer.valueOf(id)));
            return returnCertificate;
        }
        return giftCertificateService.getAll();
    }

    @PostMapping
    public void saveGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.save(giftCertificate);
        logger.info("Successfully saved new certificate into db");
    }

    //Use patch request instead of postMapping and correct method
    @PatchMapping(value = "/tagToCert/{certId}")
    public void saveTagToCert(
            @PathVariable("certId") String certId,
            @RequestBody List<Tag> tag) throws BaseException {
        giftCertificateService.addTag(Integer.valueOf(certId), tag);
        logger.info("Successfully saved new tag to certificate");
    }

    @PatchMapping("/{certId}")
    public GiftCertificate updateGiftCertificate(@PathVariable("certId") String id
            , @RequestBody Map<String, Object> patch) throws IdNotFound, InvalidIdInputInformation {
        logger.info("Updating certificate " + id);
        return giftCertificateService.update(Integer.valueOf(id), patch);
    }

    @DeleteMapping("/drop/{id}")
    public GiftCertificate deleteCertificate(@PathVariable String id) throws IdNotFound, InvalidIdInputInformation, CertificateAssociatedException {
        logger.info("Deleting certificate: " + id);
        return giftCertificateService.delete(Integer.valueOf(id));
    }
}

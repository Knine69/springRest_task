package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.BaseException;
import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Tag;
import com.jhuguet.sb_taskv1.app.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    private final Logger logger = Logger.getLogger(GiftCertificateController.class.getName());

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @ResponseBody
    @GetMapping({"/", "/{id}"})
    public List<GiftCertificate> getCertificateById(@PathVariable(required = false) String id) throws IdNotFound, InvalidIdInputInformation {
        if (id != null) {
            List<GiftCertificate> returnCertificate = new ArrayList<>();
            returnCertificate.add(giftCertificateService.get(Integer.parseInt(id)));
            return returnCertificate;
        }
        return giftCertificateService.getAll();
    }

    @ResponseBody
    @GetMapping("/byTagName/{tagName}")
    public List<GiftCertificate> getCertificateByTagName(@PathVariable("tagName") String name) {
        return giftCertificateService.getByTagName(name);
    }

    @ResponseBody
    @GetMapping("/byPart/{part}")
    public List<GiftCertificate> getCertificateByNameDescription(@PathVariable("part") String part) {
        return giftCertificateService.getByPart(part);
    }

    @ResponseBody
    @GetMapping("/byDateOrName/{sortBy}/{order}")
    public List<GiftCertificate> getCertificateByDateOrName(
            @PathVariable("sortBy") String sortBy, @PathVariable("order") String order) {
        return giftCertificateService.getByDateOrName(sortBy, order);
    }

    @PostMapping
    public GiftCertificate saveGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        logger.info("Saving new certificate into db");
        return giftCertificateService.save(giftCertificate);
    }

    @PatchMapping(value = "/updateTags/{certId}")
    public GiftCertificate updateCertificateTags(
            @PathVariable("certId") String certId,
            @RequestBody List<Tag> tag) throws BaseException {
        logger.info("Saving new tag to certificate");
        return giftCertificateService.updateTags(Integer.parseInt(certId), tag);
    }

    @PatchMapping("/{certId}")
    public GiftCertificate updateGiftCertificate(@PathVariable("certId") String id
            , @RequestBody Map<String, Object> patch) throws IdNotFound, InvalidIdInputInformation {
        logger.info("Updating certificate " + id);
        return giftCertificateService.update(Integer.parseInt(id), patch);
    }

    @DeleteMapping("/drop/{id}")
    public GiftCertificate deleteCertificate(@PathVariable String id) throws IdNotFound, InvalidIdInputInformation {
        logger.info("Deleting certificate: " + id);
        return giftCertificateService.delete(Integer.parseInt(id));
    }
}

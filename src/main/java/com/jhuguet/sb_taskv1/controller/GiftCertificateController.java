package com.jhuguet.sb_taskv1.controller;

import com.jhuguet.sb_taskv1.models.GiftCertificate;
import com.jhuguet.sb_taskv1.services.GiftCertificateService;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping(path = "/certificate")
public class GiftCertificateController {

    private Logger logger = Logger.getLogger(GiftCertificateController.class.getName());

    @Autowired
    private GiftCertificateService giftCertificateService;

    @GetMapping("/{id}")
    @ResponseBody
    public List<GiftCertificate> getCertificateById(@PathVariable(required = false) String id) {
        if(id != null){
            return new ArrayList<>((Collection) giftCertificateService.getCertificate(Integer.valueOf(id)));
        }

        return giftCertificateService.getAllCertificates();
    }

    //Create a validator to save certificate in db
    @PostMapping("/newCertificate")
    public void saveGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.saveCertificate(giftCertificate);
        //Replace sout statements with logger
        logger.info("Successfully saved new certificate into db");
    }

    //Should be PutMapping, refactor method
    @PutMapping
    public GiftCertificate updateGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        return giftCertificateService.updateCertificate(giftCertificate);
    }

    @DeleteMapping("/drop/{id}")
    public GiftCertificate deleteCertificate(@PathVariable String id){
        return giftCertificateService.deleteCertificate(Integer.valueOf(id));
    }
}

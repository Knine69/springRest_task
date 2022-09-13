package com.jhuguet.sb_taskv1.controller;

import com.jhuguet.sb_taskv1.models.GiftCertificate;
import com.jhuguet.sb_taskv1.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "certificate")
public class GiftCertificateController {
    @Autowired
    private GiftCertificateService giftCertificateService;


    @GetMapping("/all")
    @ResponseBody
    public List<GiftCertificate> getAllCertificates() {
        return giftCertificateService.getAllCertificates();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public String getCertificateById(@PathVariable String id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateService.getCertificate(Integer.valueOf(id));
        if (!certificateOptional.isPresent()) {
            return "Certificate does not exist in db";
        }

        return "Certificate retrieved: " + certificateOptional.get().getName();
    }

    @PostMapping("/save")
    public void saveGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.saveCertificate(giftCertificate);
        System.out.println("Gift certificate: " + giftCertificate.getName() + " was successfully created");
    }

    @PostMapping("/update")
    public void updateGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.saveCertificate(giftCertificate);
        System.out.println("Gift certificate: " + giftCertificate.getName() + " was successfully updated");
    }

}

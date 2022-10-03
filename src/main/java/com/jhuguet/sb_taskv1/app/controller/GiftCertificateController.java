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

/**
 * Public API GiftCertificate controller
 */
@RestController
@RequestMapping(path = "/certificate")
public class GiftCertificateController {

    private final Logger logger = Logger.getLogger(GiftCertificateController.class.getName());

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * Returns all GiftCertificates found in Database or specific GiftCertificate if ID is provided
     *
     * @param id Optional given ID to filter and return specific GiftCertificate
     * @return List of/Single GiftCertificate/s
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
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

    /**
     * Filter function used to retrieve a List of GiftCertificates based on Tag name
     *
     * @param name Tag's name used to filter
     * @return List of GiftCertificates
     */
    @ResponseBody
    @GetMapping("/byTagName/{tagName}")
    public List<GiftCertificate> getCertificateByTagName(@PathVariable("tagName") String name) {
        return giftCertificateService.getByTagName(name);
    }

    /**
     * Filter function used to retrieve a List of GiftCertificates based on part of name or description
     *
     * @param part Part of name or description that will be used to filter
     * @return List of GiftCertificates
     */
    @ResponseBody
    @GetMapping("/byPart/{part}")
    public List<GiftCertificate> getCertificateByNameDescription(@PathVariable("part") String part) {
        return giftCertificateService.getByPart(part);
    }

    /**
     * Filter function used to retrieve a List of GiftCertificates based on name or date, in ascendant
     * or descendant order
     *
     * @param sortBy Parameter given to sort by, either Date or Name
     * @param order  Order in which is needed to sort list, ascendant or descendant
     * @return List of GiftCertificates
     */
    @ResponseBody
    @GetMapping("/byDateOrName/{sortBy}/{order}")
    public List<GiftCertificate> getCertificateByDateOrName(
            @PathVariable("sortBy") String sortBy, @PathVariable("order") String order) {
        return giftCertificateService.getByDateOrName(sortBy, order);
    }


    /**
     * Will save a GiftCertificate into Database
     *
     * @param giftCertificate GiftCertificate Object which will be saved
     * @return GiftCertificate object saved into Database
     */
    @PostMapping
    public GiftCertificate saveGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        logger.info("Saving new certificate into db");
        return giftCertificateService.save(giftCertificate);
    }

    /**
     * Will patch new Set of Tags to be linked to a GiftCertificates
     *
     * @param certId Certificate ID to apply the patch for
     * @param tags   Tag list to be added to specific GiftCertificate
     * @return GiftCertificate after being patched
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    @PatchMapping(value = "/updateTags/{certId}")
    public GiftCertificate updateCertificateTags(
            @PathVariable("certId") String certId,
            @RequestBody List<Tag> tags) throws BaseException {
        logger.info("Saving new tag to certificate");
        return giftCertificateService.updateTags(Integer.parseInt(certId), tags);
    }

    /**
     * Will patch only specified changes into GiftCertificate excluding Tags
     *
     * @param id    ID of GiftCertificate to which will be applied the patch
     * @param patch Desired changes to apply
     * @return GiftCertificate after patched without Tags being patched to it
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    @PatchMapping("/{certId}")
    public GiftCertificate updateGiftCertificate(@PathVariable("certId") String id
            , @RequestBody Map<String, Object> patch) throws IdNotFound, InvalidIdInputInformation {
        logger.info("Updating certificate " + id);
        return giftCertificateService.update(Integer.parseInt(id), patch);
    }

    /**
     * Deleting function of a specific GiftCertificate
     *
     * @param id Specific ID of GiftCertificate to be deleted
     * @return GiftCertificate which was deleted
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    @DeleteMapping("/drop/{id}")
    public GiftCertificate deleteCertificate(@PathVariable String id) throws IdNotFound, InvalidIdInputInformation {
        logger.info("Deleting certificate: " + id);
        return giftCertificateService.delete(Integer.parseInt(id));
    }
}

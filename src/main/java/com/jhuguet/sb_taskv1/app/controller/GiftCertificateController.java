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

    /**
     * @param id, if given will return specific GiftCertificate that has said ID
     * @return will return pertaining List-of or single GiftCertificate retrieved from DB
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
     * @param name name of tag to search for
     * @return List of GiftCertificates which contain the tag looked for by name
     */
    @ResponseBody
    @GetMapping("/byTagName/{tagName}")
    public List<GiftCertificate> getCertificateByTagName(@PathVariable("tagName") String name) {
        return giftCertificateService.getByTagName(name);
    }

    /**
     * @param part Part of GiftCertificate's description or name which will be used to filter for
     * @return List of GiftCertificates which contain named part
     */
    @ResponseBody
    @GetMapping("/byPart/{part}")
    public List<GiftCertificate> getCertificateByNameDescription(@PathVariable("part") String part) {
        return giftCertificateService.getByPart(part);
    }

    /**
     * @param sortBy Parameter given to sort by, either Date or Name
     * @param order  Order in which is needed to sort list, ascendant or descendant
     * @return List of GiftCertificates which contain filter parameter and sort type
     */
    @ResponseBody
    @GetMapping("/byDateOrName/{sortBy}/{order}")
    public List<GiftCertificate> getCertificateByDateOrName(
            @PathVariable("sortBy") String sortBy, @PathVariable("order") String order) {
        return giftCertificateService.getByDateOrName(sortBy, order);
    }

    /**
     * @param giftCertificate GiftCertificate object to save to DB
     */
    @PostMapping
    public GiftCertificate saveGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        logger.info("Saving new certificate into db");
        return giftCertificateService.save(giftCertificate);
    }

    /**
     * @param certId Certificate ID to apply the patch for
     * @param tag    Tag list to be added to specific GiftCertificate
     * @return GiftCertificate after being patched
     * @throws BaseException extensible exception from either not found, runtime or incorrect format from inputs/process
     */
    @PatchMapping(value = "/updateTags/{certId}")
    public GiftCertificate updateCertificateTags(
            @PathVariable("certId") String certId,
            @RequestBody List<Tag> tag) throws BaseException {
        logger.info("Saving new tag to certificate");
        return giftCertificateService.updateTags(Integer.parseInt(certId), tag);
    }

    /**
     * @param id    ID of GiftCertificate to which will be applied the patch
     * @param patch Desired changes to apply
     * @return GiftCertificate after patched without Tags being patched to it
     * @throws IdNotFound                Refer to line 44
     * @throws InvalidIdInputInformation Refer to line 45
     */
    @PatchMapping("/{certId}")
    public GiftCertificate updateGiftCertificate(@PathVariable("certId") String id
            , @RequestBody Map<String, Object> patch) throws IdNotFound, InvalidIdInputInformation {
        logger.info("Updating certificate " + id);
        return giftCertificateService.update(Integer.parseInt(id), patch);
    }

    /**
     * @param id Specific ID of GiftCertificate to be deleted
     * @return Object which was deleted
     * @throws IdNotFound                Refer to line 44
     * @throws InvalidIdInputInformation Refer to line 45
     */
    @DeleteMapping("/drop/{id}")
    public GiftCertificate deleteCertificate(@PathVariable String id) throws IdNotFound, InvalidIdInputInformation {
        logger.info("Deleting certificate: " + id);
        return giftCertificateService.delete(Integer.parseInt(id));
    }
}

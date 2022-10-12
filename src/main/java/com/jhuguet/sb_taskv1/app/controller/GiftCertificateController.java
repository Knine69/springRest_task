package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidIdInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * @param id Optional given ID to look for in Database
     * @return Either List of GiftCertificates or GiftCertificate
     * @throws IdNotFound                Exception thrown when given ID is not found
     * @throws InvalidIdInputInformation Exception thrown when given ID is incorrectly entered
     */
    @ResponseBody
    @GetMapping({"/", "/{id}"})
    public List<GiftCertificate> get(@PathVariable(required = false) String id) throws IdNotFound, InvalidIdInputInformation {
        if (id != null) {
            List<GiftCertificate> returnCertificate = new ArrayList<>();
            returnCertificate.add(giftCertificateService.get(Integer.parseInt(id)));
            return returnCertificate;
        }
        return giftCertificateService.getAll();
    }

    /**
     * @param filterBy   Field used to filter by name or description
     * @param tagName    Tag name to filter in database
     * @param nameOrDate Defining parameter which accepts name, createDate or lastUpdateCase
     * @param order      Intended ascendant or descendant order of list
     * @return Filtered and/or sorted List of GiftCertificates
     */
    @ResponseBody
    @GetMapping("/getBy")
    public List<GiftCertificate> getBy(@RequestParam String filterBy, @RequestParam String tagName,
                                       @RequestParam String nameOrDate, @RequestParam String order) {

        if (!tagName.isEmpty()) {
            return giftCertificateService.getByTagName(tagName);
        }

        if (!filterBy.isEmpty()) {
            return giftCertificateService.getByPart(filterBy);
        }


        return nameOrDate.isEmpty() ? new ArrayList<>() : giftCertificateService.getByDateOrName(nameOrDate, order);
    }


    /**
     * Will save a GiftCertificate into Database
     *
     * @param giftCertificate GiftCertificate Object which will be saved
     * @return GiftCertificate object saved into Database
     */
    @PostMapping
    public GiftCertificate save(@RequestBody GiftCertificate giftCertificate) throws MissingEntity {
        logger.info("Saving new certificate into db");
        return giftCertificateService.save(giftCertificate);
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
    public GiftCertificate update(@PathVariable("certId") String id
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
    @DeleteMapping("/{id}")
    public GiftCertificate delete(@PathVariable String id) throws IdNotFound {
        logger.info("Deleting certificate: " + id);
        return giftCertificateService.delete(Integer.parseInt(id));
    }
}

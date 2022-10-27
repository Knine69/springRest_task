package com.jhuguet.sb_taskv1.app.controller;

import com.jhuguet.sb_taskv1.app.exceptions.IdNotFound;
import com.jhuguet.sb_taskv1.app.exceptions.InvalidInputInformation;
import com.jhuguet.sb_taskv1.app.exceptions.MissingEntity;
import com.jhuguet.sb_taskv1.app.models.GiftCertificate;
import com.jhuguet.sb_taskv1.app.models.Order;
import com.jhuguet.sb_taskv1.app.pages.PageResponse;
import com.jhuguet.sb_taskv1.app.services.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import java.util.List;
import java.util.Map;

/**
 * Public API GiftCertificate controller
 */
@RestController
@RequestMapping(path = "/certificates")
public class GiftCertificateController {

    private final PageResponse pageResponse = new PageResponse();

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * Will return a specific GifCertificate in database
     *
     * @param id Given ID to look for in Database
     * @return GiftCertificate
     * @throws IdNotFound              Exception thrown when given ID is not found
     * @throws InvalidInputInformation Exception thrown when given ID is incorrectly entered
     */
    @ResponseBody
    @GetMapping("/{id}")
    public GiftCertificate get(@PathVariable String id) throws IdNotFound, InvalidInputInformation {
        return giftCertificateService.get(Integer.parseInt(id));
    }

    /**
     * Will return all existing GiftCertificates in database
     *
     * @return List of GiftCertificates
     * @throws IdNotFound              Exception thrown when given ID is not found
     * @throws InvalidInputInformation Exception thrown when given ID is incorrectly entered
     */
    @ResponseBody
    @GetMapping
    public Page<GiftCertificate> getAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "3") int size,
                                        @RequestParam(defaultValue = "true") boolean asc) throws IdNotFound, InvalidInputInformation {
        pageResponse.validateInput(page, size);
        return giftCertificateService.getAllPageable(pageResponse.giveDynamicPageable(page, size, asc));
    }

    /**
     * Function used to filter and sort by specified fields
     *
     * @param partOfNameOrDescription Field used to filter by name or description
     * @param tagName                 Tag name to filter in database
     * @param nameOrDate              Defining parameter which accepts name, createDate or lastUpdateCase to sort through
     * @param order                   Intended ascendant or descendant order of list
     * @return Filtered and/or sorted List of GiftCertificates
     */
    @ResponseBody
    @GetMapping("/getBy")
    public Page<GiftCertificate> getBy(@RequestParam String partOfNameOrDescription, @RequestParam String tagName,
                                       @RequestParam String nameOrDate, @RequestParam String order,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "3") int size) throws InvalidInputInformation {
        pageResponse.validateInput(size, page);
        return giftCertificateService.filterCertificates(tagName, partOfNameOrDescription,
                nameOrDate, order, PageRequest.of(page, size));
    }


    /**
     * Will save a GiftCertificate into Database
     *
     * @param giftCertificate GiftCertificate Object which will be saved
     * @return GiftCertificate object saved into Database
     */
    @PostMapping
    public GiftCertificate save(@RequestBody GiftCertificate giftCertificate) throws MissingEntity, InvalidInputInformation {
        return giftCertificateService.save(giftCertificate);
    }


    @PostMapping("/users/{userID}")
    public Order placeNewOrder(@RequestParam List<Integer> certID,
                               @PathVariable(name = "userID") int userID) throws IdNotFound {
        return giftCertificateService
                .placeNewOrder(certID, userID);
    }

    /**
     * Will patch only specified changes into GiftCertificate excluding Tags
     *
     * @param id    ID of GiftCertificate to which will be applied the patch
     * @param patch Desired changes to apply
     * @return GiftCertificate after patched without Tags being patched to it
     * @throws IdNotFound              Exception thrown when given ID is not found
     * @throws InvalidInputInformation Exception thrown when given ID is incorrectly entered
     */
    @PatchMapping("/{certId}")
    public GiftCertificate update(@PathVariable("certId") String id
            , @RequestBody Map<String, Object> patch) throws IdNotFound, InvalidInputInformation {
        return giftCertificateService.update(Integer.parseInt(id), patch);
    }

    /**
     * Deleting function of a specific GiftCertificate
     *
     * @param id Specific ID of GiftCertificate to be deleted
     * @return GiftCertificate which was deleted
     * @throws IdNotFound Exception thrown when given ID is not found
     */
    @DeleteMapping("/{id}")
    public GiftCertificate delete(@PathVariable int id) throws IdNotFound {
        return giftCertificateService.delete(id);
    }
}

package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.MessageTemplateRequest;
import com.automate.vehicleservices.api.model.Pagination;
import com.automate.vehicleservices.common.VehicleServiceConstants;
import com.automate.vehicleservices.entity.MdOrganization;
import com.automate.vehicleservices.entity.PreDefineMessageTemplate;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.MessageTemplateRepository;
import com.automate.vehicleservices.service.dto.MessageTemplateDTO;
import com.automate.vehicleservices.service.dto.PaginatedSearchResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageTemplateService {

    private final MessageTemplateRepository messageTemplateRepository;

    public MessageTemplateService(MessageTemplateRepository messageTemplateRepository) {
        this.messageTemplateRepository = messageTemplateRepository;
    }

    @Transactional
    public MessageTemplateDTO saveAndUpdateTemplate(MessageTemplateRequest messageTemplateRequest, MdOrganization organization) {
        Date date = Calendar.getInstance().getTime();
        PreDefineMessageTemplate preDefineMessageTemplate;
        if (Objects.nonNull(messageTemplateRequest.getId())) {
            preDefineMessageTemplate = getPreDefineMessageTemplate(messageTemplateRequest.getId(), organization.getId());
        } else {
            preDefineMessageTemplate = new PreDefineMessageTemplate();
            preDefineMessageTemplate.setCreatedDatetime(date);
            preDefineMessageTemplate.setOrganization(organization);
        }
        preDefineMessageTemplate.setTemplate(messageTemplateRequest.getTemplate());
        preDefineMessageTemplate.setTemplateName(messageTemplateRequest.getTemplateName());
        preDefineMessageTemplate.setTemplateType(messageTemplateRequest.getTemplateType());
        preDefineMessageTemplate.setUpdatedDatetime(date);
        preDefineMessageTemplate.setStatus(messageTemplateRequest.getStatus());

        final var result = messageTemplateRepository.save(preDefineMessageTemplate);
        return new MessageTemplateDTO(result);
    }

    private PreDefineMessageTemplate getPreDefineMessageTemplate(int id, int orgId) {
        Optional<PreDefineMessageTemplate> preDefineMessageTemplateOptional = messageTemplateRepository.findByIdAndOrganizationId(id, orgId);
        if (!preDefineMessageTemplateOptional.isPresent())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Message Template details not found with given id " + id);
        return preDefineMessageTemplateOptional.get();
    }

    @Transactional
    public PaginatedSearchResponse<MessageTemplateDTO> fetchAllTemplateBasedOnTemplateType(int type, Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by(VehicleServiceConstants.CREATED_DATE_TIME).descending());
        final var pageResult = messageTemplateRepository.findAllByOrganizationIdAndTemplateType(orgId, type, pageable);
        final var list = pageResult.getContent().stream().map(data -> new MessageTemplateDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }

    @Transactional
    public MessageTemplateDTO fetchTemplateBasedOnId(Integer id, int orgId) {
        return new MessageTemplateDTO(getPreDefineMessageTemplate(id, orgId));
    }

    @Transactional
    public void deleteTemplateBasedOnId(Integer id, int orgId) {
        PreDefineMessageTemplate preDefineMessageTemplate = getPreDefineMessageTemplate(id, orgId);
        messageTemplateRepository.delete(preDefineMessageTemplate);
    }

    @Transactional
    public PaginatedSearchResponse<MessageTemplateDTO> search(int type, MessageTemplateRequest messageTemplateRequest, Pagination page, int orgId) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), Sort.by("created_datetime").descending());
        final var pageResult = messageTemplateRepository.search(type, messageTemplateRequest.getTemplateName(), messageTemplateRequest.getTemplate(), messageTemplateRequest.getStatus(), orgId, pageable);
        final var list = pageResult.getContent().stream().map(data -> new MessageTemplateDTO(data)).collect(Collectors.toList());
        return new PaginatedSearchResponse<>(pageResult, list);
    }
}
